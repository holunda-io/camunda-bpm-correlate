import { default as React, useCallback, useEffect, useState } from 'react';
import CorrelateMessagesTable from './components/correlate-table';
import { Configuration } from './lib/configuration';
import { Message } from './lib/message';

type CorrelateMessagesViewProps = {
  camundaRestPrefix: string;
}

function CorrelateMessagesView({ camundaRestPrefix }: CorrelateMessagesViewProps) {
  const { opLog, reload } = useOpLog(camundaRestPrefix);

  return (
    <div className="ctn-view cockpit-section-dashboard">
      <div className="dashboard-view">
        <div className="dashboard-row">
          <section className="col-xs-12 col-md-12">
            <div className="inner">
              <h1 className="section-title">Messages</h1>
              {opLog ? (
                <CorrelateMessagesTable
                  camundaRestPrefix={camundaRestPrefix}
                  messages={opLog.messages}
                  reload={reload}
                />
              ) : (
                <div>Loading...</div>
              )}
            </div>
          </section>
        </div>
      </div>
    </div>
  );
}

type OpLog = {
  messages: Message[];
  configuration: Configuration;
};

function useOpLog(camundaRestPrefix: string) {
  const [opLog, setOpLog] = useState<OpLog | null>(null);

  const loadOpLog = useCallback(async (parameters?: MessageParams) => {
    setOpLog(await fetchMessagesAndConfig(camundaRestPrefix, parameters));
  }, [camundaRestPrefix]);

  useEffect(() => {
    const parameters = { page: 0, size: 100 };
    loadOpLog(parameters);
  }, []);

  return {
    opLog,
    reload: loadOpLog
  };
}

type MessageParams = {
  page: number;
  size: number;
};

async function fetchMessagesAndConfig(camundaRestPrefix: string, parameters?: MessageParams): Promise<OpLog | null> {
  if (!parameters) {
    parameters = { page: 0, size: 100 };
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
    return null;
  }
}

export default CorrelateMessagesView;
