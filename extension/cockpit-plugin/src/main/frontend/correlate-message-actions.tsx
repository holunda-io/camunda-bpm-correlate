import React from "react";
import { useCookies } from 'react-cookie';
import { Message } from "./message";

type CorrelateMessageActionsProps = {
  camundaRestPrefix: string;
  message: Message;
  reload: () => void;
};

function CorrelateMessageActions({ camundaRestPrefix, message, reload }: CorrelateMessageActionsProps) {
  const [cookies] = useCookies(['XSRF-TOKEN']);

  const handleDelete = async () => {
    await fetch(`${camundaRestPrefix}/messages/${message.id}`, { method: 'DELETE', headers: { 'X-XSRF-TOKEN': cookies['XSRF-TOKEN'] } });
  }

  const handlePause = async () => {
    await fetch(`${camundaRestPrefix}/messages/${message.id}/pause`, { method: 'PUT', headers: { 'X-XSRF-TOKEN': cookies['XSRF-TOKEN'] } });
  }

  const handleResume = async () => {
    await fetch(`${camundaRestPrefix}/messages/${message.id}/pause`, { method: 'DELETE', headers: { 'X-XSRF-TOKEN': cookies['XSRF-TOKEN'] } });
  }

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
      <button className="btn btn-default action-button" title="Resume correlation" onClick={() => handleResume()}>
        <span className="glyphicon glyphicon-play"></span>
      </button>
    ) : (
      <button className="btn btn-default action-button" title="Pause correlation" onClick={() => handlePause()}>
        <span className="glyphicon glyphicon-pause"></span>
      </button>
    )}
    <button className="btn btn-default action-button" title="Delete message" onClick={() => handleDelete()}>
      <span className="glyphicon glyphicon-trash"></span>
    </button>
  </>);
}

export default CorrelateMessageActions;
