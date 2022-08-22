import React, {useEffect, useState} from "react";

function CorrelateMessageActions({camundaRestPrefix, message, maxRetries}) {

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
