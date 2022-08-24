import { default as React } from 'react';
import CorrelateMessagesTable from './components/correlate-table';
import { useMessages } from './lib/message';

type CorrelateMessagesViewProps = {
  camundaRestPrefix: string;
}

function CorrelateMessagesView({ camundaRestPrefix }: CorrelateMessagesViewProps) {
  const { messages, deleteMessage, pauseCorrelation, resumeCorrelation } = useMessages(camundaRestPrefix);
  // const { configuration } = useConfiguration(camundaRestPrefix);

  return (
    <div className="ctn-view cockpit-section-dashboard">
      <div className="dashboard-view">
        <div className="dashboard-row">
          <section className="col-xs-12 col-md-12">
            <div className="inner">
              <h1 className="section-title">Messages</h1>
              <CorrelateMessagesTable
                messages={messages}
                onDeleteMessage={deleteMessage}
                onPauseCorrelation={pauseCorrelation}
                onResumeCorrelation={resumeCorrelation}
              />
            </div>
          </section>
        </div>
      </div>
    </div>
  );
}

export default CorrelateMessagesView;
