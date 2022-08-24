import { useCallback, useEffect, useState } from "react";

export type MessageStatus = 'IN_PROGRESS' | 'MAX_RETRIES_REACHED' | 'PAUSED' | 'RETRYING';

export type LocalDateTimeString = string;

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

export function useMessages(camundaRestPrefix: string) {
  const [messages, setMessages] = useState<Message[]>([]);

  const loadMessages = useCallback(async (parameters?: MessageParams) => {
    setMessages(await fetchMessages(camundaRestPrefix, parameters) ?? []);
  }, [camundaRestPrefix]);

  useEffect(() => {
    loadMessages();
  }, []);

  return {
    messages,
    reload: loadMessages
  };
}

type MessageParams = {
  page: number;
  size: number;
};

async function fetchMessages(
  camundaRestPrefix: string,
  parameters: MessageParams = { page: 0, size: 100 }
): Promise<Message[] | null> {
  try {
    const response = await fetch(`${camundaRestPrefix}/messages?page=${parameters.page}&size=${parameters.size}`);
    return response.json();
  } catch (error) {
    console.error(`Fetching messages failed`, error);
    return null;
  }
}
