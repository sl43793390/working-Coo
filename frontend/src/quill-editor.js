/**
 * quill-editor：基于 Quill 2.x 的通用富文本编辑器自定义元素。
 *
 * 设计要点：
 * - 用 light DOM（不用 shadow DOM）：Quill 的 CSS 直接 import 进全局，
 *   避免样式被 shadow 边界挡住；
 * - 对外暴露 `value` 属性（HTML 字符串），编辑时派发 `value-changed` 事件，
 *   Vaadin 侧用 @Synchronize 同步，服务端 getValue() 拿到的是最新内容；
 * - 高度由外部控制：元素本身 flex column，工具栏固定高，编辑区占满剩余
 *   空间并在内部滚动。
 */
import Quill from 'quill';
import 'quill/dist/quill.snow.css';

const TOOLBAR_FULL = [
  [{ header: [1, 2, 3, false] }, 'bold', 'italic', 'underline', 'strike'],
  [{ color: [] }, { background: [] }],
  [{ list: 'ordered' }, { list: 'bullet' }, { align: [] }],
  ['blockquote', 'code-block', 'link', 'image', 'clean'],
];

const TOOLBAR_BASIC = [
  ['bold', 'italic', 'underline'],
  [{ list: 'ordered' }, { list: 'bullet' }],
  ['link', 'clean'],
];

class QuillEditor extends HTMLElement {
  connectedCallback() {
    if (this._quill) {
      return;
    }
    // 元素布局：外部可以随时改高度，工具栏始终贴顶、编辑区占满剩余空间
    this.style.display = 'flex';
    this.style.flexDirection = 'column';
    this.style.alignItems = 'stretch';

    const toolbar = document.createElement('div');
    const editor = document.createElement('div');
    this.append(toolbar, editor);

    const mode = this.getAttribute('toolbar') === 'basic' ? TOOLBAR_BASIC : TOOLBAR_FULL;
    const quill = new Quill(editor, {
      theme: 'snow',
      placeholder: this.getAttribute('placeholder') || '',
      modules: { toolbar: mode },
    });
    this._quill = quill;

    quill.on('text-change', () => {
      this._value = quill.root.innerHTML;
      this.dispatchEvent(new CustomEvent('value-changed'));
    });

    // 连接前通过 property/attribute 预设的内容，此时真正灌进编辑器
    const initial = this._pending !== undefined ? this._pending : this.getAttribute('value');
    if (initial) {
      quill.clipboard.dangerouslyPasteHTML(initial);
    }
    this._pending = undefined;
  }

  get value() {
    if (this._quill) {
      return this._quill.root.innerHTML;
    }
    return this._pending !== undefined ? this._pending : (this.getAttribute('value') || '');
  }

  set value(html) {
    const v = html == null ? '' : String(html);
    if (!this._quill) {
      this._pending = v;
      return;
    }
    // 避免服务端 setValue 与 text-change 互相触发死循环
    if (v !== this._quill.root.innerHTML) {
      this._quill.clipboard.dangerouslyPasteHTML(v);
    }
  }

  /** 纯文本（服务端展示列表列时用） */
  get plainText() {
    return this._quill ? this._quill.getText() : '';
  }
}

customElements.define('quill-editor', QuillEditor);
