function generateHTML(messages) {
  const body = messages
    .map(
      element => `<tr>
                    <td>${element.id}</td>
                    <td>${element.payloadEncoding}</td>
                  </tr>`
    )
    .join("\n");

  return `
<section>
  <div class="inner">
    <h3>Camunda Cockpit Plugin</h3>
    <table class="table table-bordered table-hover table-condensed">
      <thead>
        <tr>
          <th>Message ID</th>
          <th>Encoding</th>
        </tr>
      </thead>
      <tbody>
        ${body}
      </tbody>
    </table>
  </div>
</section>`;
}

export default {
  id: "correlate-cockpit-plugin",
  pluginPoint: "cockpit.dashboard",
  priority: 12,
  render: (container, { api }) => {
    fetch(
      `${api.cockpitApi}/plugin/correlate-cockpit-plugin/${api.engine}/messages`
    ).then(async res => {
      const messages = await res.json();
      container.innerHTML = generateHTML(messages);
    });
  }
};
