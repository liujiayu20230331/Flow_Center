INSERT INTO process_definition (process_key, process_name, bpmn_xml, status, current_version)
VALUES
  (
    'demo_leave_001',
    'Leave Request Demo',
    '<?xml version="1.0" encoding="UTF-8"?><definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL" targetNamespace="flowcenter"><process id="demo_leave_001" name="Leave Request Demo"><startEvent id="start"/><endEvent id="end"/><sequenceFlow id="flow1" sourceRef="start" targetRef="end"/></process></definitions>',
    'DRAFT',
    0
  ),
  (
    'demo_expense_001',
    'Expense Claim Demo',
    '<?xml version="1.0" encoding="UTF-8"?><definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL" targetNamespace="flowcenter"><process id="demo_expense_001" name="Expense Claim Demo"><startEvent id="start"/><endEvent id="end"/><sequenceFlow id="flow1" sourceRef="start" targetRef="end"/></process></definitions>',
    'DRAFT',
    0
  ),
  (
    'demo_purchase_001',
    'Purchase Approval Demo',
    '<?xml version="1.0" encoding="UTF-8"?><definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL" targetNamespace="flowcenter"><process id="demo_purchase_001" name="Purchase Approval Demo"><startEvent id="start"/><endEvent id="end"/><sequenceFlow id="flow1" sourceRef="start" targetRef="end"/></process></definitions>',
    'DRAFT',
    0
  )
ON DUPLICATE KEY UPDATE
  process_name = VALUES(process_name),
  bpmn_xml = VALUES(bpmn_xml),
  status = 'DRAFT',
  current_version = 0;
