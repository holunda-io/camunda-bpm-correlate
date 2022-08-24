import classNames from 'classnames';
import React from 'react';
import { Message, MessageStatus } from '../lib/message';
import CorrelateMessageActions from './correlate-message-actions';
import { DateTime } from './date-time';

type CorrelateMessagesTableProps = {
  messages: Message[];
  onDeleteMessage: (messageId: Message['id']) => Promise<void>;
  onPauseCorrelation: (messageId: Message['id']) => Promise<void>;
  onResumeCorrelation: (messageId: Message['id']) => Promise<void>;
};

function CorrelateMessagesTable({ messages, onDeleteMessage, onPauseCorrelation, onResumeCorrelation }: CorrelateMessagesTableProps) {
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
          <MessageRow
            key={message.id}
            onDeleteMessage={onDeleteMessage}
            onPauseCorrelation={onPauseCorrelation}
            onResumeCorrelation={onResumeCorrelation}
            message={message} />
        ))}
      </tbody>
    </table>
  );
}

type MessageRowProps = {
  message: Message;
  onDeleteMessage: (messageId: Message['id']) => Promise<void>;
  onPauseCorrelation: (messageId: Message['id']) => Promise<void>;
  onResumeCorrelation: (messageId: Message['id']) => Promise<void>;
};

function MessageRow({ message, onDeleteMessage, onPauseCorrelation, onResumeCorrelation }: MessageRowProps) {
  return (
    <tr>
      <td className="message-state">
        <MessageStatus status={message.status} />
      </td>
      <td className="message-id">{message.id}</td>
      <td>{message.payloadTypeNamespace}<br />.{message.payloadTypeName}</td>
      <td className="date">
        <DateTime value={message.inserted} />
      </td>
      <td>{message.retries}</td>
      <td className="date">
        {(message.nextRetry && message.status !== 'PAUSED') ? (
          <DateTime value={message.nextRetry} />
        ) : null}
      </td>
      <td>
        <CorrelateMessageActions
          message={message}
          onDeleteMessage={onDeleteMessage}
          onPauseCorrelation={onPauseCorrelation}
          onResumeCorrelation={onResumeCorrelation}
        />
      </td>
    </tr>
  );
}

type StatusProps = {
  status: MessageStatus;
};

const MessageStatus = ({ status }: StatusProps) => (<>
  <span className={classNames({
    'glyphicon glyphicon-ok-sign green': status === 'IN_PROGRESS',
    'glyphicon glyphicon-remove-sign red': status === 'MAX_RETRIES_REACHED',
    'glyphicon glyphicon-hourglass orange': status === 'PAUSED',
    'glyphicon glyphicon-circle-arrow-right blue': status === 'RETRYING'
  })} />
  <span className="sr-only">
    {status.toLowerCase().replace('_', ' ')}
  </span>
</>);

export default CorrelateMessagesTable;
