function showStacktrace(stackTrace) {
  console.log('stacktrace is: ', stackTrace);
}

function modifyNextRetry(messageId) {
  console.log('should modify the next retry for message', messageId);
}

function decreaseRetries(messageId) {
  console.log('should modify retries for message', messageId);
}

function formatDate(date) {
  if (!date) {
    return null;
  }
  let split = date.split('T');
  return split[0] + ' ' + split[1].split('.')[0];
}


function generatePage(configuration, messages) {
  let messageTableBody = messages
  .map(element => {

    let actionButtons = '';
    let nextRetry = '';
    let retries = element.retries;

    if (element.error) {
      const stackTrace = element.error ? element.error : '';
      actionButtons += `<button class="btn action-button" title="Show stacktrace" onclick="showStacktrace(stackTrace)">
        <span class="glyphicon glyphicon-warning-sign"></span>
      </button>`;
    }

    if (element.nextRetry) {
      nextRetry = formatDate(element.nextRetry);
      actionButtons += `<button class="btn action-button" title="Modify" onclick="modifyNextRetry(element.id)">
        <span class="glyphicon glyphicon-repeat"></span>
      </button>`;
    }

    if (element.retries === configuration.maxRetries) {
      actionButtons += `<button class="btn action-button" title="Decrease" onclick="decreaseRetries(element.id)">
        <span class="glyphicon glyphicon-pencil"></span>
      </button>`;
    }

    const insertedDate = formatDate(element.inserted);

    return `<tr>
               <td class="message-id">${element.id}</td>
               <td>${element.payloadTypeNamespace}<br />.${element.payloadTypeName}</td>
               <td class="date">${insertedDate}</td>
               <td>${retries}</td>
               <td class="date">${nextRetry}</td>
               <td>${actionButtons}</td>
             </tr>`;
  })
  .join('\n');

  if (messageTableBody.length === 0) {
    messageTableBody = `<tr><td colspan="6" class="no-content">No messages in the inbox</td></tr>`;
  }

  return `
<div class="ctn-view cockpit-section-dashboard">
  <div class="dashboard-view">
    <div class="dashboard-row">
      <section class="col-xs-12 col-md-12">
        <div class="inner">
          <h1 class="section-title">Messages</h1>
          <table class="cam-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Type</th>
                <th>Inserted</th>
                <th>Retries</th>
                <th>Next Retry</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              ${messageTableBody}
            </tbody>
          </table>                  
        </div>
      </section>
    </div>
  </div>
</div>`;
}

const correlateTab = {
  id: 'correlate-cockpit-plugin-route',
  pluginPoint: 'cockpit.route',
  priority: 4,
  properties: {
    path: '/correlation'
  },
  render: (container, { api }) => {
    const urlPrefix = `${api.cockpitApi}/plugin/correlate-cockpit-plugin/${api.engine}`
    fetch(
      `${urlPrefix}/configuration`
    ).then(async res => {
      const configuration = await res.json();
      fetch(
        `${urlPrefix}/messages?page=0&size=100`
      ).then(async res => {
        const json = await res.json();
        container.innerHTML = generatePage(configuration.maxRetries, json);
      }).catch(err => {
        console.error(err);
      });
    }).catch(err => {
      console.error(err);
    });
  }
};

const navigation = {
  id: 'correlate-cockpit-plugin-navigation',
  pluginPoint: 'cockpit.navigation',
  priority: 4,
  properties: {
    path: '/correlation'
  },
  render: (container) => {
    container.innerHTML = '<a href="#/correlation">Correlation</a>';
  }
};

// TODO: add metrics
const metrics = {
  id: 'correlate-cockpit-plugin-metrics',
  pluginPoint: 'cockpit.metrics',
  priority: 4,
  render: (container) => {
    container.innerHTML = '<div>Metrics</div>';
  }
};

export default [correlateTab, navigation];
