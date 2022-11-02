import { isNull } from "lodash-es";
import React, { FormEvent, useId, useState } from 'react';
import { formatLocalIsoDateTime, formatUtcIsoDateTime } from "../lib/date";
import { Message, MessageRetry } from "../lib/message";
import { Modal, ModalBody, ModalFooter, ModalHeader, ModalTitle } from "./modal";

type EditRetriesModalProps = {
  message: Message;
  maxRetries: number;
  onClose: () => void;
  onSubmit: (messageId: Message['id'], retry: MessageRetry) => void;
};

export const EditRetriesModal = ({ message, onClose, onSubmit, maxRetries }: EditRetriesModalProps) => {
  const [retries, setRetries] = useState(message.retries);
  const [nextRetry, setNextRetry] = useState(message.nextRetry);
  const retriesInputId = useId();
  const nextRetryInputId = useId();

  if (isNull(message)) {
    return null;
  }

  const handleSubmit = (event: FormEvent) => {
    event.preventDefault();

    if (isNaN(retries) || isNull(nextRetry)) {
      return;
    }

    onSubmit(message.id, { retries, nextRetry });
  }

  return (
    <form onSubmit={handleSubmit}>
      <Modal isOpen={true}>
        <ModalHeader>
          <ModalTitle>Message retry</ModalTitle>
        </ModalHeader>
        <ModalBody>
          {/* <span>
          Message could not be correlated from the first attempt. It started the retrying
          and already has used {messageRetry.retries} out of {maxRetries}.
        </span> */}

          {/* Message next due: {messageRetry.nextRetry} */}

          <form>
            <div className="form-group">
              <label htmlFor={retriesInputId}>Retries</label>
              <input
                type="number"
                className="form-control"
                id={retriesInputId}
                value={retries}
                onChange={event => setRetries(event.target.valueAsNumber)}
                min={0}
                max={maxRetries}
                required />
            </div>
            <div className="form-group">
              <label htmlFor={nextRetryInputId}>Next Retry</label>
              <input
                type="datetime-local"
                className="form-control"
                id={nextRetryInputId}
                value={formatLocalIsoDateTime(nextRetry) ?? undefined}
                onChange={event => setNextRetry(formatUtcIsoDateTime(event.target.value))}
                required />
            </div>
          </form>
        </ModalBody>
        <ModalFooter>
          <button className={'btn btn-default'} onClick={onClose}>Close</button>
          <button type="submit" className={'btn btn-primary'}>Submit</button>
        </ModalFooter>
      </Modal>
    </form>
  )
}
