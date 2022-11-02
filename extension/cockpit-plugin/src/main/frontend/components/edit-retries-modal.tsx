import { isNull } from "lodash-es";
import React, { useState } from 'react';
import { Message, MessageRetry } from "../lib/message";
import { Modal, ModalBody, ModalFooter, ModalHeader, ModalTitle } from "./modal";

type EditRetriesModalProps = {
  message: Message | null;
  maxRetries: number;
  onClose: () => void;
  onSubmit: (messageId: Message['id'], retry: MessageRetry) => void;
};

export const EditRetriesModal = ({ message, onClose, onSubmit, maxRetries }: EditRetriesModalProps) => {

  const retries = useState( message?.retries)

  if (isNull(message)) {
    return null;
  }
  const messageId = message.id
  const messageRetry: MessageRetry = {
    retries: message.retries,
    nextRetry: message.nextRetry!!
  };

  return (
    <Modal isOpen={!isNull(message)}>
      <ModalHeader>
        <ModalTitle>Message retry</ModalTitle>
      </ModalHeader>
      <ModalBody>
        <span>
          Message could not be correlated from the first attempt. It started the retrying
          and already has used { messageRetry.retries } out of { maxRetries }.
        </span>

        Message next due: {messageRetry.nextRetry}
      </ModalBody>
      <ModalFooter>
        <button className={'btn btn-default'} onClick={onClose}>Close</button>
        <button className={'btn btn-primary'} onClick={() => onSubmit(messageId, messageRetry)}>Submit</button>
      </ModalFooter>
    </Modal>
  )
}
