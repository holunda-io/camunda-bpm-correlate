import React, {useEffect, useState} from "react";
import CorrelateMessagesTable from "./correlate-table";
import CorrelateMessageActions from "./correlate-message-actions";

function formatDate(date) {
  if (!date) {
    return null;
  }
  let split = date.split('T');
  return split[0] + ' ' + split[1].split('.')[0];
}

function CorrelateMessagesView({camundaRestPrefix}) {

  const [opLog, setOpLog] = useState();

  useEffect(() => {
    fetch(
      `${camundaRestPrefix}/configuration`
    ).then(async res => {
      const configuration = await res.json();
      fetch(
        `${camundaRestPrefix}/messages?page=0&size=100`
      ).then(async res => {
        setOpLog({messages: await res.json(), configuration: configuration});
      }).catch(err => {
        console.error(err);
      });
    }).catch(err => {
      console.error(err);
    });
  });

  if (!opLog) {
    return <div>Loading...</div>;
  }

  return (<div class="ctn-view cockpit-section-dashboard">
    <div class="dashboard-view">
      <div class="dashboard-row">
        <section class="col-xs-12 col-md-12">
          <div class="inner">
            <h1 class="section-title">Messages</h1>
            <CorrelateMessagesTable children={
              opLog.messages
                .map(element => {
                  const insertedDate = formatDate(element.inserted);
                  let nextRetry = '';
                  if (element.nextRetry) {
                    nextRetry = formatDate(element.nextRetry);
                  }
                  return (<tr>
                    <td class="message-id">{element.id}</td>
                    <td>{element.payloadTypeNamespace}<br/>.{element.payloadTypeName}</td>
                    <td class="date">{insertedDate}</td>
                    <td>{element.retries}</td>
                    <td class="date">{nextRetry}</td>
                    <td><CorrelateMessageActions camundaRestPrefix={camundaRestPrefix} message={element} maxRetries={opLog.configuration.maxRetries}/></td>
                  </tr>);
                })
            }/>
          </div>
        </section>
      </div>
    </div>
  </div>);
}

export default CorrelateMessagesView;
