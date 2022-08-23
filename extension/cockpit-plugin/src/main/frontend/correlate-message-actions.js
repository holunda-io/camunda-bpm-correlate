import React, {useEffect, useState} from "react";

function CorrelateMessageActions({camundaRestPrefix, message, maxRetries, reload}) {

  const handleDelete = async () => {
    await fetch(`${camundaRestPrefix}/messages/${message.id}`, { method: 'DELETE' })
    reload();
  }

  return (<span>
    {message.error ? (<button class="btn btn-default action-button" title="Show stacktrace" onClick={() => showStacktrace(message.error)}>
      <span class="glyphicon glyphicon-warning-sign"></span>
    </button>) : null}
    {message.nextRetry ? (<button className="btn btn-default action-button" title="Modify next retry" onClick={() => modifyNextRetry(message.id)}>
      <span className="glyphicon glyphicon-repeat"></span>
    </button>) : null}
    {message.retries === maxRetries ? (<button className="btn btn-default action-button" title="Decrease retry count" onClick={() => decreaseRetries(message.id)}>
      <span className="glyphicon glyphicon-pencil"></span>
    </button>) : null}
    <button className="btn btn-default action-button" title="Delete message" onClick={() => handleDelete()}>
      <span className="glyphicon glyphicon-trash"></span>
    </button>
  </span>);
}

function showStacktrace(stackTrace) {
  console.log('stacktrace', stackTrace);
}

function modifyNextRetry(messageId) {
  console.log('nextRetry', messageId);
}

function decreaseRetries(messageId) {
  console.log('decrease', messageId);
}


export default CorrelateMessageActions;
