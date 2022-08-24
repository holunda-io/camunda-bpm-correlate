import React from 'react';
import CorrelateMessageActions from './correlate-message-actions';


function CorrelateMessagesTable({ messages, camundaRestPrefix }) {

  return (<table className="cam-table">
    <thead>
    <tr>
      <th>State</th>
      <th>ID</th>
      <th>Type</th>
      <th>Inserted</th>
      <th>Retries</th>
      <th>Next Retry</th>
      <th>Actions</th>
    </tr>
    </thead>
    <tbody>
    {!messages || messages.length === 0 ? (
      <tr>
        <td colSpan="7" className="no-content">No messages to correlate in the inbox</td>
      </tr>
    ) : null}
    {messages?.map(element => (
      <MessageRow camundaRestPrefix={camundaRestPrefix} element={element} />
    ))}
    </tbody>
  </table>);
}

function MessageRow({ camundaRestPrefix, element }) {
  return (
    <tr>
      <td className="message-state">{statusToGlyph(element.status)}</td>
      <td className="message-id">{element.id}</td>
      <td>{element.payloadTypeNamespace}<br />.{element.payloadTypeName}</td>
      <td className="date">{formatDate(element.inserted)}</td>
      <td>{element.retries}</td>
      <td className="date">{element.nextRetry && element.status !== 'PAUSED' ? formatDate(element.nextRetry) : null}</td>
      <td><CorrelateMessageActions camundaRestPrefix={camundaRestPrefix} message={element} /></td>
    </tr>
  );
}

function formatDate(date) {
  if (!date) {
    return null;
  }
  let split = date.split('T');
  return split[0] + ' ' + split[1].split('.')[0];
}

function statusToGlyph(state) {
  let stateClass = [];
  console.log('State is: ', state);
  switch (state) {
    case 'IN_PROGRESS':
      stateClass = ['glyphicon', 'glyphicon-ok-sign', 'green'];
      break;
    case 'MAX_RETRIES_REACHED':
      stateClass = ['glyphicon', 'glyphicon-remove-sign', 'red'];
      break;
    case 'PAUSED':
      stateClass = ['glyphicon', 'glyphicon-hourglass', 'orange'];
      break;
    case 'RETRYING':
      stateClass = ['glyphicon', 'glyphicon-circle-arrow-right', 'blue'];
      break;
    default:
      break;
  }

  return <span className={stateClass.join(' ')}></span>;
}

export default CorrelateMessagesTable;
