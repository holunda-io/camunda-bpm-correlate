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
            <td colSpan={7} className="no-content">No messages to correlate in the inbox</td>
          </tr>
    ) : null}
        {messages?.map(message => (
          <MessageRow key={message.id} reload={reload} camundaRestPrefix={camundaRestPrefix} message={message} />
        ))}
    </tbody>
  </table>);
}


function MessageRow({ camundaRestPrefix, message, reload }) {
  return (
    <tr>
      <td className="message-state">{statusToGlyph(message.status)}</td>
      <td className="message-id">{message.id}</td>
      <td>{message.payloadTypeNamespace}<br />.{message.payloadTypeName}</td>
      <td className="date">{formatDate(message.inserted)}</td>
      <td>{message.retries}</td>
      <td className="date">{message.nextRetry && message.status !== 'PAUSED' ? formatDate(message.nextRetry) : null}</td>
      <td><CorrelateMessageActions reload={reload} camundaRestPrefix={camundaRestPrefix} message={message} /></td>
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
