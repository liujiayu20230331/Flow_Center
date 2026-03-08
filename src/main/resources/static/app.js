function customTranslate(template, replacements) {
  const translations = {
    'Append Task': '追加任务',
    'Append User Task': '追加人工任务',
    'Append EndEvent': '追加结束事件',
    'Append Gateway': '追加网关',
    'Append Intermediate/Boundary Event': '追加中间/边界事件',
    'Append expanded SubProcess': '追加展开子流程',
    'Append {type}': '追加 {type}',
    'Create {type}': '创建 {type}',
    'Create StartEvent': '创建开始事件',
    'Create EndEvent': '创建结束事件',
    'Create Task': '创建任务',
    'Create UserTask': '创建人工任务',
    'Create Gateway': '创建网关',
    'Create DataObjectReference': '创建数据对象',
    'Create expanded SubProcess': '创建展开子流程',
    'Start Event': '开始事件',
    'End Event': '结束事件',
    'Task': '任务',
    'User Task': '人工任务',
    'Exclusive Gateway': '排他网关',
    'Parallel Gateway': '并行网关',
    'Inclusive Gateway': '包容网关',
    'Event based Gateway': '事件网关',
    'Sub Process': '子流程',
    'Expanded Sub Process': '展开子流程',
    'Intermediate Throw Event': '中间抛出事件',
    'Intermediate Catch Event': '中间捕获事件',
    'Message Start Event': '消息开始事件',
    'Timer Start Event': '定时开始事件',
    'Message End Event': '消息结束事件',
    'Terminate End Event': '终止结束事件',
    'Conditional Start Event': '条件开始事件',
    'Signal Start Event': '信号开始事件',
    'Call Activity': '调用活动',
    'Data Object Reference': '数据对象引用',
    'Send Task': '发送任务',
    'Receive Task': '接收任务',
    'Manual Task': '手工任务',
    'Business Rule Task': '业务规则任务',
    'Script Task': '脚本任务',
    'Service Task': '服务任务',
    'Link Intermediate Catch Event': '链接捕获事件',
    'Link Intermediate Throw Event': '链接抛出事件',
    'Message Intermediate Catch Event': '消息捕获事件',
    'Message Intermediate Throw Event': '消息抛出事件',
    'Signal Intermediate Catch Event': '信号捕获事件',
    'Signal Intermediate Throw Event': '信号抛出事件',
    'Timer Intermediate Catch Event': '定时捕获事件',
    'Conditional Intermediate Catch Event': '条件捕获事件',
    'Compensation Intermediate Throw Event': '补偿抛出事件',
    'Delete': '删除',
    'Remove': '移除',
    'Create Pool/Participant': '创建泳池/参与者',
    'Create Lane': '创建泳道',
    'Create Group': '创建分组',
    'Create TextAnnotation': '创建文本注释',
    'Participant': '参与者',
    'Lane': '泳道',
    'Group': '分组',
    'Text Annotation': '文本注释',
    'Sequence Flow': '顺序流',
    'Message Flow': '消息流',
    'Association': '关联线',
    'is not allowed to move boundary event': '不允许移动边界事件',
    'Drop zone': '放置区域',
    'Connect using Sequence/MessageFlow or Association': '使用顺序流/消息流/关联线连接',
    'Connect using Association': '使用关联线连接',
    'Change type': '修改类型',
    'Replace': '替换',
    'Activate the hand tool': '启用抓手工具',
    'Activate the lasso tool': '启用套索工具',
    'Activate the create/remove space tool': '启用留白/缩减工具',
    'Activate the global connect tool': '启用全局连线工具',
    'Direct Editing': '直接编辑',
    'Show/Hide': '显示/隐藏',
    'Open minimap': '打开缩略图',
    'Close minimap': '关闭缩略图',
    'no parent for {element} in {parent}': '{parent} 中缺少 {element} 的父元素',
    'no shape type specified': '未指定图形类型',
    'flow elements must be children of pools/participants': '流程元素必须属于泳池/参与者',
    'elements must have a parent': '元素必须有父级',
    businessObject: '业务对象'
  };
  const text = translations[template] || template;
  if (!replacements) {
    return text;
  }
  return text.replace(/\{([^}]+)\}/g, function (_, key) {
    return replacements[key] || '';
  });
}

const customTranslateModule = {
  translate: ['value', customTranslate]
};

const DEFAULT_XML = [
  '<?xml version="1.0" encoding="UTF-8"?>',
  '<bpmn:definitions xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"',
  '  xmlns:bpmn="http://www.omg.org/spec/BPMN/20100524/MODEL"',
  '  xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI"',
  '  xmlns:dc="http://www.omg.org/spec/DD/20100524/DC"',
  '  xmlns:di="http://www.omg.org/spec/DD/20100524/DI"',
  '  id="Definitions_1"',
  '  targetNamespace="http://bpmn.io/schema/bpmn">',
  '  <bpmn:process id="Process_1" isExecutable="true">',
  '    <bpmn:startEvent id="StartEvent_1" name="开始" />',
  '  </bpmn:process>',
  '  <bpmndi:BPMNDiagram id="BPMNDiagram_1">',
  '    <bpmndi:BPMNPlane id="BPMNPlane_1" bpmnElement="Process_1">',
  '      <bpmndi:BPMNShape id="_BPMNShape_StartEvent_2" bpmnElement="StartEvent_1">',
  '        <dc:Bounds x="173" y="102" width="36" height="36" />',
  '      </bpmndi:BPMNShape>',
  '    </bpmndi:BPMNPlane>',
  '  </bpmndi:BPMNDiagram>',
  '</bpmn:definitions>'
].join('\n');

const modeler = new BpmnJS({
  container: '#canvas',
  additionalModules: [customTranslateModule],
  keyboard: { bindTo: window }
});

const state = {
  currentId: null,
  currentStatus: 'DRAFT',
  currentVersion: 0,
  versions: [],
  dirty: false,
  currentView: 'list'
};

const element = {
  workspace: document.querySelector('.workspace'),
  menuItems: Array.from(document.querySelectorAll('.menu-item')),
  processList: document.getElementById('processList'),
  processKey: document.getElementById('processKey'),
  processName: document.getElementById('processName'),
  searchName: document.getElementById('searchName'),
  searchStatus: document.getElementById('searchStatus'),
  workspaceName: document.getElementById('workspaceName'),
  workspaceMeta: document.getElementById('workspaceMeta'),
  currentStatus: document.getElementById('currentStatus'),
  currentVersion: document.getElementById('currentVersion'),
  currentId: document.getElementById('currentId'),
  detailSummary: document.getElementById('detailSummary'),
  versionList: document.getElementById('versionList'),
  versionStageTitle: document.getElementById('versionStageTitle'),
  versionStageMeta: document.getElementById('versionStageMeta'),
  versionStageList: document.getElementById('versionStageList'),
  saveButton: document.getElementById('saveButton'),
  publishButton: document.getElementById('publishButton'),
  disableButton: document.getElementById('disableButton'),
  deleteButton: document.getElementById('deleteButton'),
  backListButton: document.getElementById('backListButton'),
  fitButton: document.getElementById('fitButton'),
  newButton: document.getElementById('newButton'),
  refreshListButton: document.getElementById('refreshListButton'),
  toast: document.getElementById('toast')
};

let toastTimer = null;

function showToast(message, isError) {
  element.toast.textContent = message;
  element.toast.className = 'toast show' + (isError ? ' error' : '');
  if (toastTimer) {
    window.clearTimeout(toastTimer);
  }
  toastTimer = window.setTimeout(function () {
    element.toast.className = 'toast';
  }, 2600);
}

async function request(url, options) {
  const response = await fetch(url, options);
  const payload = await response.json();
  if (!response.ok || payload.code !== 0) {
    throw new Error(payload.message || '请求失败');
  }
  return payload.data;
}

async function saveXml() {
  const result = await modeler.saveXML({ format: true });
  return result.xml;
}

async function importXml(xml) {
  await modeler.importXML(xml);
  fitViewport();
}

function fitViewport() {
  modeler.get('canvas').zoom('fit-viewport', 'auto');
}

function getStatusClass(status) {
  if (status === 'PUBLISHED') return 'status-badge status-published';
  if (status === 'DISABLED') return 'status-badge status-disabled';
  return 'status-badge status-draft';
}

function getStatusText(status) {
  if (status === 'PUBLISHED') return '已发布';
  if (status === 'DISABLED') return '已停用';
  return '草稿';
}

function setMenu(view) {
  element.menuItems.forEach(function (item) {
    item.classList.toggle('active', item.getAttribute('data-menu') === view);
  });
}

function updateRoute() {
  const params = new URLSearchParams();
  if ((state.currentView === 'detail' || state.currentView === 'versions') && state.currentId != null) {
    params.set('page', state.currentView);
    params.set('id', String(state.currentId));
  } else {
    params.set('page', 'list');
  }
  window.history.replaceState({}, '', window.location.pathname + '?' + params.toString());
}

function switchView(view) {
  state.currentView = view;
  element.workspace.classList.toggle('detail-mode', view === 'detail');
  element.workspace.classList.toggle('versions-mode', view === 'versions');
  element.backListButton.classList.toggle('hidden', view === 'list');
  if (view === 'detail') setMenu('designer');
  else if (view === 'versions') setMenu('versions');
  else setMenu('list');
  updateRoute();
}

function updateHeader() {
  const name = element.processName.value.trim();
  const key = element.processKey.value.trim();
  if (state.currentView === 'detail' && state.currentId != null) {
    element.workspaceName.textContent = (name || '审批流') + '详情页';
    element.workspaceMeta.textContent = key ? '当前正在查看审批流详情，流程编码：' + key : '当前正在查看审批流详情。';
  } else if (state.currentView === 'versions' && state.currentId != null) {
    element.workspaceName.textContent = (name || '审批流') + '版本记录';
    element.workspaceMeta.textContent = '当前聚焦查看该审批流的版本历史、部署信息与发布轨迹。';
  } else {
    element.workspaceName.textContent = '审批流列表';
    element.workspaceMeta.textContent = '首页默认展示审批流列表，点击流程后进入对应审批流详情页。';
  }
  element.currentStatus.textContent = getStatusText(state.currentStatus || 'DRAFT');
  element.currentVersion.textContent = String(state.currentVersion || 0);
  element.currentId.textContent = state.currentId == null ? '新建' : String(state.currentId);
  element.detailSummary.textContent = state.currentId == null ? '尚未选择流程，当前处于新建模式。' : '已进入当前审批流详情页，可继续保存、发布、停用或查看版本记录。';
  element.publishButton.disabled = state.currentId == null;
  element.disableButton.disabled = state.currentId == null || state.currentStatus !== 'PUBLISHED';
  element.deleteButton.disabled = state.currentId == null || state.currentStatus !== 'DRAFT';
}

function renderVersions() {
  if (!state.versions.length) {
    element.versionList.innerHTML = '<div class="empty">暂无版本记录。首次发布后会在这里显示部署信息。</div>';
    element.versionStageTitle.textContent = (element.processName.value.trim() || '审批流') + '版本记录';
    element.versionStageMeta.textContent = '当前审批流还没有发布版本。';
    element.versionStageList.innerHTML = '<div class="empty">当前审批流还没有发布版本。</div>';
    return;
  }
  const html = state.versions.map(function (item) {
    return [
      '<div class="version-card">',
      '  <div class="version-card-header">',
      '    <strong>V' + item.version + '</strong>',
      '    <span class="' + getStatusClass('PUBLISHED') + '">已部署</span>',
      '  </div>',
      '  <div class="version-id">部署 ID：' + escapeHtml(item.deploymentId) + '</div>',
      '  <div class="version-id">流程定义 ID：' + escapeHtml(item.processDefinitionId) + '</div>',
      '  <p class="version-time">创建时间：' + escapeHtml(item.createdAt || '-') + '</p>',
      '</div>'
    ].join('');
  }).join('');
  element.versionList.innerHTML = html;
  element.versionStageTitle.textContent = (element.processName.value.trim() || '审批流') + '版本记录';
  element.versionStageMeta.textContent = '共 ' + state.versions.length + ' 个已发布版本，以下为部署与定义信息。';
  element.versionStageList.innerHTML = html;
}

function renderProcessList(records) {
  if (!records.length) {
    element.processList.innerHTML = '<div class="empty">暂无流程记录。先在右侧画布新建一条流程草稿。</div>';
    return;
  }
  element.processList.innerHTML = records.map(function (item) {
    const active = state.currentId === item.id ? ' active' : '';
    return [
      '<div class="process-card' + active + '" data-id="' + item.id + '">',
      '  <div class="process-card-header">',
      '    <div class="process-name">' + escapeHtml(item.processName) + '</div>',
      '    <span class="' + getStatusClass(item.status) + '">' + escapeHtml(getStatusText(item.status)) + '</span>',
      '  </div>',
      '  <div class="process-key">' + escapeHtml(item.processKey) + '</div>',
      '  <div class="meta">版本 ' + escapeHtml(String(item.currentVersion)) + ' · 更新于 ' + escapeHtml(item.updatedAt || '-') + '</div>',
      '</div>'
    ].join('');
  }).join('');
}

function escapeHtml(value) {
  return String(value)
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;')
    .replace(/'/g, '&#39;');
}

async function loadList() {
  const data = await request('/process/list', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      pageNum: 1,
      pageSize: 100,
      processName: element.searchName.value.trim(),
      status: element.searchStatus.value
    })
  });
  renderProcessList(data.records || []);
}

async function loadDetail(id) {
  const detail = await request('/process/detail', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ id: Number(id) })
  });
  state.currentId = detail.id;
  state.currentStatus = detail.status;
  state.currentVersion = detail.currentVersion;
  state.versions = detail.versions || [];
  state.dirty = false;
  element.processKey.value = detail.processKey || '';
  element.processName.value = detail.processName || '';
  element.processKey.disabled = true;
  switchView('detail');
  await importXml(detail.bpmnXml || DEFAULT_XML);
  updateHeader();
  renderVersions();
  await loadList();
}

async function resetDesigner() {
  state.currentId = null;
  state.currentStatus = 'DRAFT';
  state.currentVersion = 0;
  state.versions = [];
  state.dirty = false;
  element.processKey.disabled = false;
  element.processKey.value = '';
  element.processName.value = '';
  switchView('list');
  await importXml(DEFAULT_XML);
  updateHeader();
  renderVersions();
  await loadList();
}

async function handleSave() {
  const processKey = element.processKey.value.trim();
  const processName = element.processName.value.trim();
  if (!processKey || !processName) {
    showToast('请先填写流程编码和流程名称。', true);
    return;
  }
  const xml = await saveXml();
  const data = await request('/process/save', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ id: state.currentId, processKey: processKey, processName: processName, bpmnXml: xml })
  });
  state.currentId = data.processId;
  state.currentStatus = data.status;
  state.dirty = false;
  element.processKey.disabled = true;
  updateHeader();
  await loadDetail(data.processId);
  showToast('流程草稿已保存。');
}

async function handlePublish() {
  if (state.currentId == null) {
    showToast('请先保存流程草稿。', true);
    return;
  }
  if (state.dirty) {
    await handleSave();
  }
  await request('/process/publish', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ id: state.currentId })
  });
  await loadDetail(state.currentId);
  showToast('流程已发布，并生成新版本。');
}

async function handleDisable() {
  if (state.currentId == null) {
    showToast('当前没有可停用的流程。', true);
    return;
  }
  await request('/process/disable', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ id: state.currentId })
  });
  await loadDetail(state.currentId);
  showToast('流程已停用。');
}

async function handleDelete() {
  if (state.currentId == null) {
    showToast('当前没有可删除的流程。', true);
    return;
  }
  if (!window.confirm('仅草稿状态允许删除，确认删除当前草稿吗？')) {
    return;
  }
  await request('/process/delete', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ id: state.currentId })
  });
  await resetDesigner();
  showToast('流程草稿已删除。');
}

function bindEvents() {
  element.saveButton.addEventListener('click', function () { handleSave().catch(handleError); });
  element.publishButton.addEventListener('click', function () { handlePublish().catch(handleError); });
  element.disableButton.addEventListener('click', function () { handleDisable().catch(handleError); });
  element.deleteButton.addEventListener('click', function () { handleDelete().catch(handleError); });
  element.backListButton.addEventListener('click', function () { switchView('list'); updateHeader(); });
  element.fitButton.addEventListener('click', fitViewport);
  element.newButton.addEventListener('click', function () { resetDesigner().catch(handleError); });
  element.refreshListButton.addEventListener('click', function () { loadList().catch(handleError); });
  element.searchName.addEventListener('keydown', function (event) { if (event.key === 'Enter') loadList().catch(handleError); });
  element.searchStatus.addEventListener('change', function () { loadList().catch(handleError); });
  element.processName.addEventListener('input', updateHeader);
  element.processKey.addEventListener('input', updateHeader);
  element.menuItems.forEach(function (item) {
    item.addEventListener('click', function () {
      const menu = item.getAttribute('data-menu');
      if (menu === 'list') {
        switchView('list');
        updateHeader();
        return;
      }
      if (menu === 'versions') {
        if (state.currentId == null) {
          showToast('请先从审批流列表选择一条流程，再查看版本记录。', true);
          return;
        }
        switchView('versions');
        updateHeader();
        renderVersions();
        return;
      }
      if (state.currentId == null) {
        showToast('请先从审批流列表选择一条流程，再进入详情页。', true);
        return;
      }
      switchView('detail');
      updateHeader();
    });
  });
  element.processList.addEventListener('click', function (event) {
    const card = event.target.closest('.process-card');
    if (!card) return;
    loadDetail(card.getAttribute('data-id')).catch(handleError);
  });
  modeler.on('commandStack.changed', function () { state.dirty = true; });
}

function handleError(error) {
  showToast(error.message || '操作失败', true);
}

async function initialize() {
  bindEvents();
  await importXml(DEFAULT_XML);
  const params = new URLSearchParams(window.location.search);
  const page = params.get('page');
  const id = params.get('id');
  if ((page === 'detail' || page === 'versions') && id) {
    await loadDetail(id);
    if (page === 'versions') {
      switchView('versions');
      updateHeader();
      renderVersions();
    }
    return;
  }
  switchView('list');
  updateHeader();
  renderVersions();
  await loadList();
}

initialize().catch(handleError);
