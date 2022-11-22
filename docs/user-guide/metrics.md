In order to be able to monitor the library in operations, we expose different metrics using 
standard Spring Boot approach and use Micrometer as the library. By doing so, we provide maximum
flexibility for the integration of the metrics in your monitoring system (like Prometheus or others).

The following metrics are provided:

| Component   | Name                                           | Type    | Tags    | Description                                                                                                                     |
|-------------|------------------------------------------------|---------|---------|---------------------------------------------------------------------------------------------------------------------------------|
| Ingress     | camunda_bpm_correlate_ingress_received_total   | counter | channel | Total number of messages received via given channel.                                                                            |
| Ingress     | camunda_bpm_correlate_ingress_accepted_total   | counter | channel | Total number of messages accepted via channel.                                                                                  |
| Ingress     | camunda_bpm_correlate_ingress_ignored_total    | counter | channel | Total number of messages received but ignored via channel.                                                                      | 
| Acceptor    | camunda_bpm_correlate_acceptor_persisted_total | counter |         | Total number of messages persisted in the inbox.                                                                                | 
| Acceptor    | camunda_bpm_correlate_acceptor_dropped_total   | counter |         | Total number of messages dropped instead of persisting in the inbox.                                                            | 
| Inbox       | camunda_bpm_correlate_inbox_message            | gauge   | status  | Number of messages in the inbox by status. The statuses are: total, retrying, in_progress, error, maxRetriesReached and paused. | 
| Correlation | camunda_bpm_correlate_correlation_success      | counter |         | Total number of messages successfully correlated.                                                                               | 
| Correlation | camunda_bpm_correlate_correlation_error        | counter |         | Total number of messages correlated with error.                                                                                 |



