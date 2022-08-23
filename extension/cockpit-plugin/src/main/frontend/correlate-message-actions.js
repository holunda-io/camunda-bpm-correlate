import React from "react";
import {useCookies} from 'react-cookie';

function CorrelateMessageActions({camundaRestPrefix, message, reload}) {

  const [cookies] = useCookies(['XSRF-TOKEN']);

  const handleDelete = async () => {
    await fetch(`${camundaRestPrefix}/messages/${message.id}`, {method: 'DELETE', headers: {'X-XSRF-TOKEN': cookies['XSRF-TOKEN']}});
  }

  const handlePause = async () => {
    await fetch(`${camundaRestPrefix}/messages/${message.id}/pause`, {method: 'PUT', headers: {'X-XSRF-TOKEN': cookies['XSRF-TOKEN']}});
  }

  const handleResume = async () => {
    await fetch(`${camundaRestPrefix}/messages/${message.id}/pause`, {method: 'DELETE', headers: {'X-XSRF-TOKEN': cookies['XSRF-TOKEN']}});
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


  return (<span>
    {message.error ? (<button class="btn btn-default action-button" title="Show stacktrace" onClick={() => showStacktrace()}>
      <span class="glyphicon glyphicon-warning-sign"></span>
    </button>) : null}
    {message.nextRetry ? (<button className="btn btn-default action-button" title="Modify next retry" onClick={() => modifyNextRetry()}>
      <span className="glyphicon glyphicon-repeat"></span>
    </button>) : null}
    {message.status === 'MAX_RETRIES_REACHED' ? (
      <button className="btn btn-default action-button" title="Decrease retry count" onClick={() => decreaseRetries()}>
        <span className="glyphicon glyphicon-pencil"></span>
      </button>) : null}
    <button className="btn btn-default action-button" title="Delete message" onClick={() => handleDelete()}>
      <span className="glyphicon glyphicon-trash"></span>
    </button>
    {message.status === 'PAUSED' ? (<button className="btn btn-default action-button" title="Resume correlation" onClick={() => handleResume()}>
      <span className="glyphicon glyphicon-play"></span>
    </button>) : (<button className="btn btn-default action-button" title="Pause correlation" onClick={() => handlePause()}>
      <span className="glyphicon glyphicon-pause"></span>
    </button>)}


  </span>);
}

export default CorrelateMessageActions;
