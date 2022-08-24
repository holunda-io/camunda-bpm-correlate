export type MessageStatus = 'IN_PROGRESS' | 'MAX_RETRIES_REACHED' | 'PAUSED' | 'RETRYING';

type LocalDateTimeString = string;

export type Message = {
  id: string;
  status: MessageStatus;
  payloadEncoding: string;
  payloadTypeNamespace: string;
  payloadTypeName: string;
  payloadTypeRevision: string | null;
  inserted: LocalDateTimeString;
  timeToLiveDuration: string | null;
  expiration: LocalDateTimeString | null;
  retries: number;
  nextRetry: LocalDateTimeString | null;
  error: string | null;
};
