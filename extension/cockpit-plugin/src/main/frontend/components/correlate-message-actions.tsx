import React from "react";
import { Message } from "../lib/message";
import { Button } from "./button";

type CorrelateMessageActionsProps = {
  message: Message;
  onDeleteMessage: (messageId: Message['id']) => unknown;
  onPauseCorrelation: (messageId: Message['id']) => unknown;
  onResumeCorrelation: (messageId: Message['id']) => unknown;
  onShowStacktrace: (messageId: Message['id']) => unknown;
};

function CorrelateMessageActions({ message, onDeleteMessage, onPauseCorrelation, onResumeCorrelation, onShowStacktrace }: CorrelateMessageActionsProps) {
  const modifyNextRetry = () => {
    console.log('nextRetry', message.id);
  }

  const decreaseRetries = () => {
    console.log('decrease', message.id);
  }

  return (<>
    {message.error ? (
      <Button label="Show stacktrace" icon="align-left" onClick={() => onShowStacktrace(message.id)} />
    ) : null}

    {message.status === 'MAX_RETRIES_REACHED' || message.nextRetry ? (
      <Button label="Edit retry details" icon="pencil" onClick={() => decreaseRetries()} />
    ) : null}

    {message.status === 'PAUSED' ? (
      <Button label="Resume correlation" icon="play" onClick={() => onResumeCorrelation(message.id)} />
    ) : (
      <Button label="Pause correlation" icon="pause" onClick={() => onPauseCorrelation(message.id)} />
    )}

    <Button label="Delete message" icon="trash" onClick={() => onDeleteMessage(message.id)} />
  </>);
}

export default CorrelateMessageActions;
