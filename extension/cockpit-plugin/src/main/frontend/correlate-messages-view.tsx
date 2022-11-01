import { default as React } from 'react';
import CorrelateMessagesTable from './components/correlate-table';
import { StacktraceModal } from './components/stacktrace-modal';
import { useMessages } from './lib/message';
import { useQueryParam } from './lib/query-params';

type CorrelateMessagesViewProps = {
  camundaRestPrefix: string;
}

function CorrelateMessagesView({ camundaRestPrefix }: CorrelateMessagesViewProps) {
  const { messages, deleteMessage, pauseCorrelation, resumeCorrelation } = useMessages(camundaRestPrefix);
  // const { configuration } = useConfiguration(camundaRestPrefix);

  const [stacktraceModalMessageId, setStacktraceModalMessageId] = useQueryParam('stacktrace');
  const stacktraceMessage = messages?.find(({ id }) => stacktraceModalMessageId === id) ?? null;

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
                />
              ) : (
                <div>Loading...</div>
              )}
            </div>
            <StacktraceModal message={stacktraceMessage} onClose={() => setStacktraceModalMessageId(null)} />
          </section>
        </div>
      </div>
    </div>
  );
}

export default CorrelateMessagesView;
