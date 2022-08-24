import classNames from 'classnames';
import React from 'react';
import CorrelateMessageActions from './correlate-message-actions';
import { Message, MessageStatus } from './message';

type CorrelateMessagesTableProps = {
  camundaRestPrefix: string;
  messages: Message[];
  reload: () => void;
};

function CorrelateMessagesTable({ messages, camundaRestPrefix, reload }: CorrelateMessagesTableProps) {
  return (
    <table className="cam-table">
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
    </table>
  );
}

type MessageRowProps = {
  camundaRestPrefix: string;
  message: Message;
  reload: () => void;
};

function MessageRow({ camundaRestPrefix, message, reload }: MessageRowProps) {
  return (
    <tr>
      <td className="message-state">
        <MessageStatus status={message.status} />
      </td>
      <td className="message-id">{message.id}</td>
      <td>{message.payloadTypeNamespace}<br />.{message.payloadTypeName}</td>
      <td className="date">{formatDate(message.inserted)}</td>
      <td>{message.retries}</td>
      <td className="date">{message.nextRetry && message.status !== 'PAUSED' ? formatDate(message.nextRetry) : null}</td>
      <td><CorrelateMessageActions reload={reload} camundaRestPrefix={camundaRestPrefix} message={message} /></td>
    </tr>
  );
}

function formatDate(date: string | null) {
  if (!date) {
    return null;
  }
  const split = date.split('T');
  return split[0] + ' ' + split[1].split('.')[0];
}

type StatusProps = {
  status: MessageStatus;
};

const MessageStatus = ({ status }: StatusProps) => (
  <span className={classNames({
    'glyphicon glyphicon-ok-sign green': status === 'IN_PROGRESS',
    'glyphicon glyphicon-remove-sign red': status === 'MAX_RETRIES_REACHED',
    'glyphicon glyphicon-hourglass orange': status === 'PAUSED',
    'glyphicon glyphicon-circle-arrow-right blue': status === 'RETRYING'
  })} />
);

export default CorrelateMessagesTable;
