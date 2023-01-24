import { isNull } from 'lodash-es';
import React, { FormEvent, useId, useState } from 'react';
import { formatLocalIsoDateTime, formatUtcIsoDateTime } from '../lib/date';
import { Message, MessageRetry } from '../lib/message';
import { Modal, ModalBody, ModalFooter, ModalHeader, ModalTitle } from './modal';

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

    console.log('Handle Submit clicked');

    event.preventDefault();

    if (isNaN(retries) || isNull(nextRetry)) {
      return;
    }

    onSubmit(message.id, { retries, nextRetry });
  }

  return (
    <Modal isOpen={true}>
      <ModalHeader>
        <ModalTitle>Message retry</ModalTitle>
      </ModalHeader>
      <ModalBody>
        <form>
          <span>Message could not be correlated from the first attempt.</span><br />
          {retries >= maxRetries ?
              <div className="form-group">
                <span>All ${maxRetries} are exhausted. To continue retries please decrease the number of retries below:</span>
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
            : <div>There are still {maxRetries - retries} / {maxRetries} retries available.</div>
          }
          <span>Next correlation will take place at {formatLocalIsoDateTime(nextRetry)}. You can change it by changing the value below:</span>
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
        <button className={'btn btn-primary'} onClick={handleSubmit} type="submit">Submit</button>
      </ModalFooter>
    </Modal>
  )
}
