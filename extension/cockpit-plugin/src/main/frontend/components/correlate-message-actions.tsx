import React from "react";
import { Message } from "../lib/message";

type CorrelateMessageActionsProps = {
  message: Message;
  onDeleteMessage: (messageId: Message['id']) => Promise<void>;
  onPauseCorrelation: (messageId: Message['id']) => Promise<void>;
  onResumeCorrelation: (messageId: Message['id']) => Promise<void>;
};

function CorrelateMessageActions({ message, onDeleteMessage, onPauseCorrelation, onResumeCorrelation }: CorrelateMessageActionsProps) {
  const showStacktrace = () => {
    console.log('stacktrace', message.error);
  }

  const modifyNextRetry = () => {
    console.log('nextRetry', message.id);
  }

  const decreaseRetries = () => {
    console.log('decrease', message.id);
  }

  return (<>
    {message.error ? (
      <button className="btn btn-default action-button" title="Show stacktrace" onClick={() => showStacktrace()}>
        <span className="glyphicon glyphicon-align-left"></span>
      </button>
    ) : null}
    {message.status === 'MAX_RETRIES_REACHED' || message.nextRetry ? (
      <button className="btn btn-default action-button" title="Edit retry details" onClick={() => decreaseRetries()}>
        <span className="glyphicon glyphicon-pencil"></span>
      </button>
    ) : null}
    {message.status === 'PAUSED' ? (
      <button className="btn btn-default action-button" title="Resume correlation" onClick={() => onResumeCorrelation(message.id)}>
        <span className="glyphicon glyphicon-play"></span>
      </button>
    ) : (
      <button className="btn btn-default action-button" title="Pause correlation" onClick={() => onPauseCorrelation(message.id)}>
        <span className="glyphicon glyphicon-pause"></span>
      </button>
    )}
    <button className="btn btn-default action-button" title="Delete message" onClick={() => onDeleteMessage(message.id)}>
      <span className="glyphicon glyphicon-trash"></span>
    </button>
  </>);
}

export default CorrelateMessageActions;
