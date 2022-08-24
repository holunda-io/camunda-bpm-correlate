import React, {useCallback, useEffect, useState} from 'react';
import CorrelateMessagesTable from './correlate-table';

function CorrelateMessagesView({camundaRestPrefix}) {

  const {opLog, reload} = useOpLog(camundaRestPrefix);

  return (<div className="ctn-view cockpit-section-dashboard">
    <div className="dashboard-view">
      <div className="dashboard-row">
        <section className="col-xs-12 col-md-12">
          <div className="inner">
            <h1 className="section-title">Messages</h1>
            {opLog ?
              <CorrelateMessagesTable
                camundaRestPrefix={camundaRestPrefix}
                messages={opLog.messages}
                reload={reload}
              /> : <div>Loading...</div>
            }
          </div>
        </section>
      </div>
    </div>
  </div>);
}

function useOpLog(camundaRestPrefix) {
  const [opLog, setOpLog] = useState();

  const loadOpLog = useCallback(async (parameters) => {
    setOpLog(await fetchMessagesAndConfig(camundaRestPrefix, parameters));
  }, [camundaRestPrefix]);

  useEffect(() => {
    const parameters = {page: 0, size: 100};
    loadOpLog(parameters);
  }, []);

  return {
    opLog,
    reload: loadOpLog
  };
}

async function fetchMessagesAndConfig(camundaRestPrefix, parameters) {
  if (!parameters) {
    parameters = {page: 0, size: 100};
  }
  try {
    const [configurationRes, messagesRes] = await Promise.all(
      [
        fetch(
          `${camundaRestPrefix}/configuration`
        ),
        fetch(
          `${camundaRestPrefix}/messages?page=${parameters.page}&size=${parameters.size}`
        )
      ]
    );
    return {
      messages: await messagesRes.json(),
      configuration: await configurationRes.json()
    };
  } catch (error) {
    console.error(error);
  }
}

export default CorrelateMessagesView;
