package com.sl.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sl.entity.TaskDependency;

/**
 * 依赖环路检测（T2-3）。
 * <p>
 * 只用 Mockito 顶掉 Mapper，不启 Spring、不连库 ——
 * 环路检测是纯图算法，本来也不该依赖数据库才能验证。
 */
class TaskDependencyServiceTest {

	private TaskDependencyService svc;
	private final List<TaskDependency> graph = new ArrayList<>();

	@BeforeEach
	void setUp() {
		svc = new TaskDependencyService();
		com.sl.mapper.TaskDependencyMapper mapper = mock(com.sl.mapper.TaskDependencyMapper.class);
		when(mapper.selectByProject(anyString())).thenReturn(graph);
		when(mapper.countPair(anyString(), anyString())).thenReturn(0L);
		// 反射注入私有字段，避免为了测试把字段改成包可见
		org.springframework.test.util.ReflectionTestUtils.setField(svc, "taskDependencyMapper", mapper);
	}

	private static TaskDependency edge(String pre, String task) {
		TaskDependency d = new TaskDependency();
		d.setIdDependency(pre + "->" + task);
		d.setIdProject("P1");
		d.setIdPreTask(pre);
		d.setIdTask(task);
		return d;
	}

	@Test
	void rejectsSelfDependency() {
		assertEquals(TaskDependencyService.ERR_SELF, svc.addDependency("P1", "A", "A"));
	}

	@Test
	void allowsSimpleChain() {
		// 空图上加 A->B
		assertNull(svc.addDependency("P1", "B", "A"));
	}

	@Test
	void blocksDirectCycle() {
		// 已有 A -> B，再加 B -> A 就是环
		graph.add(edge("A", "B"));
		assertEquals(TaskDependencyService.ERR_CYCLE, svc.addDependency("P1", "A", "B"));
	}

	@Test
	void blocksIndirectCycle() {
		// A -> B -> C，再加 C -> A 是三元环，只有 BFS/DFS 才查得出来
		graph.add(edge("A", "B"));
		graph.add(edge("B", "C"));
		assertEquals(TaskDependencyService.ERR_CYCLE, svc.addDependency("P1", "A", "C"));
	}

	@Test
	void allowsDiamond() {
		// 菱形不是环：A->B, A->C, B->D, C->D
		graph.add(edge("A", "B"));
		graph.add(edge("A", "C"));
		graph.add(edge("B", "D"));
		assertNull(svc.addDependency("P1", "D", "C"));
	}

	@Test
	void reachableFollowsDirection() {
		graph.add(edge("A", "B"));
		assertTrue(svc.reachable("P1", "A", "B"));
		assertFalse(svc.reachable("P1", "B", "A"), "边是有向的，反向不该可达");
	}

	@Test
	void findCycleReturnsEmptyForAcyclicGraph() {
		graph.add(edge("A", "B"));
		graph.add(edge("B", "C"));
		assertTrue(svc.findCycle("P1").isEmpty());
	}

	@Test
	void findCycleDetectsExistingCycle() {
		graph.add(edge("A", "B"));
		graph.add(edge("B", "C"));
		graph.add(edge("C", "A"));
		List<String> cycle = svc.findCycle("P1");
		assertFalse(cycle.isEmpty(), "三元环必须被检出");
		assertNotNull(cycle);
	}
}
