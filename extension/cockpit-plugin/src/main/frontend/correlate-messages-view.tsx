import { isNull } from 'lodash-es';
import { default as React } from 'react';
import CorrelateMessagesTable from './components/correlate-table';
import { EditRetriesModal } from './components/edit-retries-modal';
import { StacktraceModal } from './components/stacktrace-modal';
import { useConfiguration } from './lib/configuration';
import { Message, MessageRetry, useMessages } from './lib/message';
import { useQueryParam } from './lib/query-params';

type CorrelateMessagesViewProps = {
  camundaRestPrefix: string;
}

function CorrelateMessagesView({ camundaRestPrefix }: CorrelateMessagesViewProps) {
  const { messages, deleteMessage, pauseCorrelation, resumeCorrelation, changeRetries } = useMessages(camundaRestPrefix);
  const { configuration } = useConfiguration(camundaRestPrefix);

  const [stacktraceModalMessageId, setStacktraceModalMessageId] = useQueryParam('stacktrace');
  const [editRetriesModalMessageId, setEditRetriesModalMessageId] = useQueryParam('edit-retries');
  const stacktraceMessage = messages?.find(({ id }) => stacktraceModalMessageId === id) ?? null;
  const editMessage = messages?.find(({ id }) => editRetriesModalMessageId === id) ?? null;

  const handleSubmit = async (messageId: Message['id'], messageRetry: MessageRetry) => {
    try {
      await changeRetries(messageId, messageRetry);
      setEditRetriesModalMessageId(null);
    } catch (error) {
      console.error('Error setting retries', error);
    }
  }

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

            {isNull(stacktraceMessage) ? null : (
              <StacktraceModal
                message={stacktraceMessage}
                onClose={() => setStacktraceModalMessageId(null)}
              />
            )}

            {isNull(configuration) || isNull(editMessage) ? null : (
              <EditRetriesModal
                message={editMessage}
                maxRetries={configuration.maxRetries}
                onSubmit={handleSubmit}
                onClose={() => setEditRetriesModalMessageId(null)}
              />
            )}
          </section>
        </div>
      </div>
    </div>
  );
}

export default CorrelateMessagesView;
