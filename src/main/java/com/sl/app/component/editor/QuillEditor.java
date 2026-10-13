package com.sl.app.component.editor;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Synchronize;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;

/**
 * 通用富文本编辑器，基于 Quill 2.x。
 * <p>
 * 为什么不直接用 Vaadin 自带的 RichTextEditor：它是商业组件，
 * 未配 license 时编辑器上会浮官方水印。这里自己包一层开源的 Quill 2：
 * <ul>
 *   <li>前端实现：frontend/src/quill-editor.js（light DOM + snow 主题）</li>
 *   <li>对外只暴露一个 value 属性（HTML 字符串），编辑时派发 value-changed，
 *       服务端 getValue() 拿到的始终是最新内容（@Synchronize 同步）</li>
 *   <li>高度由外部 setHeight 控制：工具栏贴顶，编辑区占满剩余空间并内部滚动</li>
 * </ul>
 * 用法：
 * <pre>
 * QuillEditor editor = new QuillEditor().withHeight("220px");
 * editor.setValue("&lt;p&gt;hello&lt;/p&gt;");
 * String html = editor.getValue();
 * </pre>
 */
@Tag("quill-editor")
@JsModule("./src/quill-editor.js")
@NpmPackage(value = "quill", version = "2.0.3")
public class QuillEditor extends Component implements HasSize, HasStyle {

	private static final long serialVersionUID = 1L;

	public QuillEditor() {
	}

	public QuillEditor(String html) {
		setValue(html);
	}

	/** 当前内容（HTML）。编辑器内每次变更都会同步回服务端 */
	@Synchronize(property = "value", value = "value-changed")
	public String getValue() {
		return getElement().getProperty("value", "");
	}

	public void setValue(String html) {
		getElement().setProperty("value", null == html ? "" : html);
	}

	public QuillEditor withValue(String html) {
		setValue(html);
		return this;
	}

	public QuillEditor withPlaceholder(String placeholder) {
		getElement().setAttribute("placeholder", placeholder == null ? "" : placeholder);
		return this;
	}

	/**
	 * 工具栏精简程度：full（默认，带标题/颜色/图片等）或 basic（仅加粗斜体列表链接）。
	 * 必须在编辑器初始化（attach 到界面）前设置。
	 */
	public QuillEditor withToolbar(String fullOrBasic) {
		getElement().setAttribute("toolbar", fullOrBasic);
		return this;
	}

	public QuillEditor withHeight(String height) {
		setHeight(height);
		return this;
	}

	/** 内容是否为空（纯文本层面，空段落 <p><br></p> 也算空） */
	public boolean isEmpty() {
		return htmlToPlainText(getValue()).isEmpty();
	}

	/**
	 * HTML 转纯文本，用于在表格列 / 纯文本场景展示富文本内容。
	 * 只做展示级别的清洗，不追求还原所有实体。
	 */
	public static String htmlToPlainText(String html) {
		if (null == html || html.isBlank()) {
			return "";
		}
		String s = html.replaceAll("(?s)<[^>]*>", " ");
		s = s.replace("&nbsp;", " ")
				.replace("&amp;", "&")
				.replace("&lt;", "<")
				.replace("&gt;", ">")
				.replace("&quot;", "\"");
		return s.replaceAll("\\s+", " ").trim();
	}
}
