import React from "react";


function CorrelateMessagesTable({ children }) {

  if (!children || children.length === 0) {
    children = (<tr>
      <td colSpan="6" className="no-content">No messages in the inbox</td>
    </tr>);
  }

  return (<table className="cam-table">
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
    <tbody>{children}</tbody>
  </table>);
}

export default CorrelateMessagesTable;
