import React from "react";
import CorrelateMessageActions from "./correlate-message-actions";


function CorrelateMessagesTable({ messages, maxRetries, camundaRestPrefix }) {
  return (<table className="cam-table">
    <thead>
    <tr>
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
        <td colSpan="6" className="no-content">No messages in the inbox</td>
      </tr>
    ) : null}
    {messages?.map(element => (
      <MessageRow camundaRestPrefix={camundaRestPrefix} maxRetries={maxRetries} element={element} />
    ))}
    </tbody>
  </table>);
}

function MessageRow({ camundaRestPrefix, maxRetries, element }) {
  return (
    <tr>
      <td className="message-id">{element.id}</td>
      <td>{element.payloadTypeNamespace}<br/>.{element.payloadTypeName}</td>
      <td className="date">{formatDate(element.inserted)}</td>
      <td>{element.retries}</td>
      <td className="date">{element.nextRetry ? formatDate(element.nextRetry) : null}</td>
      <td><CorrelateMessageActions camundaRestPrefix={camundaRestPrefix} message={element} maxRetries={maxRetries}/></td>
    </tr>
  )
}

function formatDate(date) {
  if (!date) {
    return null;
  }
  let split = date.split('T');
  return split[0] + ' ' + split[1].split('.')[0];
}

export default CorrelateMessagesTable;
