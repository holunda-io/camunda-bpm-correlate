import { default as React } from 'react';
import CorrelateMessagesTable from './components/correlate-table';
import { StacktraceModal } from './components/stacktrace-modal';
import { useMessages } from './lib/message';
import { useQueryParam } from './lib/query-params';
import { EditRetriesModal } from './components/edit-retries-modal';
import { useConfiguration } from './lib/configuration';

type CorrelateMessagesViewProps = {
  camundaRestPrefix: string;
}

function CorrelateMessagesView({ camundaRestPrefix }: CorrelateMessagesViewProps) {
  const { messages, deleteMessage, pauseCorrelation, resumeCorrelation, changeRetries } = useMessages(camundaRestPrefix);
  const { configuration } = useConfiguration(camundaRestPrefix);

  const [stacktraceModalMessageId, setStacktraceModalMessageId] = useQueryParam('stacktrace');
  const [editRetriesModalMessageId, setEditRetriesModalMessageId] = useQueryParam('edit-retries');
  const stacktraceMessage = messages?.find(({ id }) => stacktraceModalMessageId === id) ?? null;
  const editMessage = messages?.find(({ id }) => editRetriesModalMessageId === id);

  return (
    <div className="ctn-view cockpit-section-dashboard">
      <div className="dashboard-view">
        <div className="dashboard-row">
          <section className="col-xs-12 col-md-12">
            <div className="inner">
              <h1 className="section-title">Messages</h1>
              {messages ? (
                <CorrelateMessagesTable
                  messages={messages}
                  onDeleteMessage={deleteMessage}
                  onPauseCorrelation={pauseCorrelation}
                  onResumeCorrelation={resumeCorrelation}
                  onShowStacktrace={setStacktraceModalMessageId}
                  onEditRetries={setEditRetriesModalMessageId}
                />
              ) : (
                <div>Loading...</div>
              )}
            </div>
            <StacktraceModal message={stacktraceMessage}
                             onClose={() => setStacktraceModalMessageId(null)}
            />
            <EditRetriesModal message={editMessage}
                              maxRetries={ configuration?.maxRetries ?? -1 } // FIXME? how to default here
                              onSubmit={(messageId, messageRetry) => changeRetries(messageId, messageRetry)
                                .then(() => setEditRetriesModalMessageId(null))
                                .catch((error) => console.log('Error setting retries', error))
                              }
                              onClose={() => setEditRetriesModalMessageId(null)}
            />
          </section>
        </div>
      </div>
    </div>
  );
}

export default CorrelateMessagesView;
