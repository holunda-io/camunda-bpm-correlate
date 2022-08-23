import React, {useEffect, useState, useCallback} from "react";
import CorrelateMessagesTable from "./correlate-table";

function CorrelateMessagesView({camundaRestPrefix}) {
  const { opLog, reload } = useOpLog(camundaRestPrefix);

  if (!opLog) {
    return <div>Loading...</div>;
  }

  return (<div class="ctn-view cockpit-section-dashboard">
    <div class="dashboard-view">
      <div class="dashboard-row">
        <section class="col-xs-12 col-md-12">
          <div class="inner">
            <h1 class="section-title">Messages</h1>
            <CorrelateMessagesTable
                camundaRestPrefix={camundaRestPrefix}
                maxRetries={opLog.configuration.maxRetries}
                messages={opLog.messages}
                reload={reload}
            />
          </div>
        </section>
      </div>
    </div>
  </div>);
}

function useOpLog(camundaRestPrefix) {
  const [opLog, setOpLog] = useState();

  const loadOpLog = useCallback(async () => {
    setOpLog(await loadMessages(camundaRestPrefix));
  }, [camundaRestPrefix])

  useEffect(() => {
    loadOpLog();
  }, []);

  return {
    opLog,
    reload: loadOpLog
  };
}

async function loadMessages(camundaRestPrefix) {
  try {
    const [configurationRes, messagesRes] = await Promise.all([
                        fetch(
                          `${camundaRestPrefix}/configuration`
                        ),
                        fetch(
                          `${camundaRestPrefix}/messages?page=0&size=100`
                        )
                      ]);
    return {
      messages: await messagesRes.json(),
      configuration: await configurationRes.json(),
    }
  } catch (error) {
    console.error(error);
  }
}

export default CorrelateMessagesView;
