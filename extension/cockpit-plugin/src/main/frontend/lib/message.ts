import { useCallback, useEffect, useState } from "react";
import { useCookies } from "react-cookie";

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
  const [cookies] = useCookies(['XSRF-TOKEN']);
  const headers = { 'X-XSRF-TOKEN': cookies['XSRF-TOKEN'] };

  const [messages, setMessages] = useState<Message[]>([]);

  const loadMessages = useCallback(async (parameters?: MessageParams) => {
    setMessages(await fetchMessages(camundaRestPrefix, parameters) ?? []);
  }, [camundaRestPrefix]);

  const deleteMessage = useCallback(async (messageId: Message['id']) => {
    await fetch(`${camundaRestPrefix}/messages/${messageId}`, { method: 'DELETE', headers });
    loadMessages();
  }, [camundaRestPrefix, headers]);

  const pauseCorrelation = useCallback(async (messageId: Message['id']) => {
    await fetch(`${camundaRestPrefix}/messages/${messageId}/pause`, { method: 'PUT', headers });
    loadMessages();
  }, [camundaRestPrefix, headers]);

  const resumeCorrelation = useCallback(async (messageId: Message['id']) => {
    await fetch(`${camundaRestPrefix}/messages/${messageId}/pause`, { method: 'DELETE', headers });
    loadMessages();
  }, [camundaRestPrefix, headers]);

  useEffect(() => {
    loadMessages();
  }, []);

  return {
    messages,
    deleteMessage,
    pauseCorrelation,
    resumeCorrelation
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
