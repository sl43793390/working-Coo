# Working Coo

Working Coo is a project & task collaboration system built on Vaadin 24 + Spring Boot 3. It covers project management, the full task lifecycle, RBAC permissions, schedules & to-dos, notification reminders, statistics charts (Gantt / Burndown), file management and operation auditing.

> In 2026-10 the project was upgraded from JDK 17→21, Spring Boot 2.x→3.5.0 and Vaadin 23→24.10,
> followed by two rounds of enhancements: Phase 2 (kanban / dependencies / notifications / milestones /
> Gantt / burndown / search / Excel / audit / schedule) and Phase 3 (BCrypt / login throttling /
> unit tests / Actuator / containerization).
> 中文文档：[README.md](README.md)

## Feature Overview

After login the left side shows an accordion navigation (menus are maintained in database tables, so adding/removing menus needs no code change):

| Menu Group | Features |
| --- | --- |
| Project Management | My projects (list / copy / delete / recycle bin), new project, project statistics, milestones, project members (OWNER / MEMBER / VIEWER), project documents (upload & download) |
| Task Management | My tasks (claim / edit / comment / delete), new task, tasks I assigned, task statistics & analysis, task kanban (drag to change status), Gantt chart, burndown chart, task import/export (Excel) |
| My To-dos | Global search (MySQL ngram full-text index), schedule (day / week / month views + to-do list panel), schedule list |
| User Management | User list, role management (RBAC with a tree-shaped role-permission picker) |
| Backend | System configuration, project recycle bin (restore / purge) |

Key capabilities:

- **Notification center**: in-app bell with unread badge (15s polling auto-refresh), covering task assignment / comment mentions / approaching deadlines / milestones / schedule reminders; no duplicate pushes for the same object & type
- **Permission model**: two layers — global RBAC (user-role-permission via Shiro) plus in-project roles (OWNER can edit the project and manage members, MEMBER can edit tasks, VIEWER is read-only); all delete operations are restricted to "admin or the creator", enforced again on the server side
- **Task dependencies**: predecessor → successor with BFS cycle detection on creation
- **Gantt / Burndown**: Gantt highlights today with no horizontal scrollbar; burndown chart has tooltips and a Y axis anchored at 0; the calculation lives in `BurnDownCalculator` and is unit-testable
- **Operation audit**: `@AuditLog` annotation + AOP persists to the `operation_log` table, shown as a timeline on the task detail page
- **Security**: BCrypt password hashing (legacy SM3 hashes are silently upgraded on successful login), account lockout for 15 minutes after 5 failed logins

## Tech Stack

| Category | Choice | Version |
| --- | --- | --- |
| Java | JDK | 21 |
| Framework | Spring Boot | 3.5.0 |
| UI | Vaadin (Flow, Vite build) | 24.10.10 |
| ORM | MyBatis-Plus | 3.5.15 |
| Pagination | PageHelper | 4.1.3 |
| Database | MySQL | 8.x (mysql-connector-j 8.3.0) |
| Connection pool | HikariCP | via Spring Boot |
| Security | Apache Shiro (jakarta flavor) + Ehcache | 2.0.6 |
| Logging | log4j2 (async, console + rolling files, traceId) | 2.24.3 |
| Utilities | Hutool, EasyExcel, Apache Commons | - |

## Requirements

- JDK 21
- Maven 3.9+
- MySQL 8.x (connection configured in `src/main/resources/application-dev.properties`)

## Run Locally

```bash
mvn spring-boot:run
```

Then open **http://localhost:8081/work** (`server.port=${PORT:8081}`; the context path is fixed to `/work`; the port can be overridden with the `PORT` environment variable).

Alternatively, import the project into your IDE (IntelliJ IDEA / Eclipse / VS Code) and run `com.sl.Application`.

## Docker (Recommended)

```bash
docker compose up -d          # starts app + mysql (with health checks & ordering)
docker compose logs -f app    # application logs
docker compose down           # stop (volumes preserved)
docker compose down -v        # stop and wipe data volumes
```

Open **http://localhost:8081/work**. Uploaded files and application logs live in the `app-storage` / `app-logs` volumes and survive container recreation.

Build the image standalone:

```bash
docker build -t workingcoo:latest .
docker run -p 8081:8081 \
  -e CLIENT1URL='jdbc:mysql://host:3306/project?useUnicode=true&characterEncoding=utf8&useSSL=false' \
  -e DBDEMO1USERNAME=root -e DBDEMO1PASSWORD=xxx \
  workingcoo:latest
```

The Dockerfile is a multi-stage build: stage 1 runs `mvn -Pproduction` with Maven + JDK 21 (including the Vaadin frontend build), stage 2 ships a JRE-only runtime image.

> JDK 17+ note: EasyExcel 3.x uses cglib and requires
> `--add-opens java.base/java.lang=ALL-UNNAMED`, otherwise Excel import/export throws
> `InaccessibleObjectException`. Already wired via `JAVA_OPTS` in the Dockerfile / docker-compose.

## Production Build

```bash
mvnw clean package -Pproduction      # Windows
./mvnw clean package -Pproduction    # Mac & Linux
```

The build produces a fat JAR including all dependencies and frontend assets:

```bash
java --add-opens java.base/java.lang=ALL-UNNAMED \
     -jar target/workingCoo-1.0-SNAPSHOT.jar
```

## Database Script Policy

- Single authoritative script: `src/main/resources/institutions.sql` (full schema + menu/dictionary data)
- It is updated manually once per release and imported by the deployer; the application **does not** run automatic migrations on startup
- Adding a menu = inserts into the three menu tables in `institutions.sql`; the component-class column of a menu row points to its Vaadin component

## Project Layout

```
src/main/java/com/sl
├── Application.java          # entry point (@Theme mytodo / Lumo LIGHT, @EnableScheduling)
├── app/
│   ├── appLayout/            # main layout & left navigation
│   ├── views/                # views (LoginView, AppLayoutAccordionView main shell)
│   ├── component/util/       # shared component utilities (ComFactory, TabsUtil, ComponentUtil)
│   └── controller/           # backend endpoints (FileController for downloads)
├── project/                  # projects (create, statistics, recycle bin, documents, milestones, members)
├── task/                     # tasks (create, update, detail, comments, kanban, Gantt, burndown, Excel)
├── user/                     # user & role management (RBAC, role-permission tree)
├── schedule/                 # schedule (day/week/month views, to-do list panel, drag to reschedule)
├── search/                   # global search
├── notify/                   # notification bell + reminder scheduler (@Scheduled)
├── security/                 # password hashing, login throttling, Shiro credentials matcher
├── aop/                      # @AuditLog operation-history aspect
├── service/                  # service layer (TaskProcess, ProjectProcess, ProjectRoleService, ...)
├── mapper/                   # MyBatis mapper interfaces
├── entity/                   # database entities
├── bo/                       # business objects (TreeNode, BurnDownSeries, SearchHit, ...)
├── config/                   # configuration (Shiro, MyBatis, traceId filter, StorageProperties)
└── util/                     # utilities (Util, DateInfo, ...)

src/main/resources
├── application.properties    # main config (port /work, MyBatis-Plus, Actuator, file storage)
├── application-dev.properties# dev datasource & connection pool
├── institutions.sql          # single authoritative database script
├── log4j2-spring.xml         # logging config (traceId output)
└── mapping/*.xml             # MyBatis mapper XML

frontend/themes/mytodo/       # Vaadin theme (styles.css + component-level CSS)
```

Uploaded files are stored under `${user.home}/.workingcoo/storage` by default; override with the `APP_STORAGE_DIR` environment variable (set to `/app/storage` in the Docker deployment).

## Tests

```bash
mvn test
```

28 test cases (`src/test/java`), all in-memory with Mockito — no MySQL required:

| Test class | Coverage |
| --- | --- |
| `PasswordHasherTest` | BCrypt / SM3 compatibility and silent upgrade |
| `LoginAttemptServiceTest` | login throttling (lock & unlock) |
| `TaskDependencyServiceTest` | BFS dependency cycle detection |
| `BurnDownCalculatorTest` | burndown remaining-hours semantics, tasks without due dates, timeline |
| `TaskExcelServiceTest` | row-by-row Excel import validation |

Tests touching EasyExcel need `--add-opens`, already configured in surefire's `argLine`.

## Troubleshooting

- **Port**: `server.port=${PORT:8081}` — override with the `PORT` env var; the context path is always `/work`
- **EasyExcel throws `InaccessibleObjectException`**: JDK 17+ module encapsulation; add `--add-opens java.base/java.lang=ALL-UNNAMED`
- **ngram full-text search misses Chinese**: verify `ngram_token_size` and that the index was actually created
- **New menu item not showing**: menus come from database tables; missing menu/permission rows simply don't render
- **Actuator security**: only `health / info / metrics` are exposed; with the Shiro filter chain `/** = anon` these endpoints are unauthenticated — restrict `/actuator/**` to an internal network at your gateway / Nginx in production
- **Startup hangs at HikariCP**: MySQL unreachable — an environment issue, not a code issue
- **traceId**: `X-Trace-Id` flows through log4j2 logs; grep the value from the response header inside `logs/` to correlate a request

## Roadmap

See [readMe2.md](readMe2.md).
