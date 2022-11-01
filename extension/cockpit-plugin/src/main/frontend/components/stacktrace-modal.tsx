import { isNull } from "lodash-es";
import React from "react";
import { Message } from "../lib/message";
import { Modal, ModalBody, ModalFooter, ModalHeader, ModalTitle } from "./modal";

type StacktraceModalProps = {
  message: Message | null;
  onClose: () => void;
};

export const StacktraceModal = ({ message, onClose }: StacktraceModalProps) => {
  if (isNull(message)) {
    return null;
  }

  return (
    <Modal isOpen={!isNull(message)}>
      <ModalHeader>
        <ModalTitle>Details</ModalTitle>
      </ModalHeader>
      <ModalBody>
        {message.error}
      </ModalBody>
      <ModalFooter>
        <button onClick={onClose}>Close</button>
      </ModalFooter>
    </Modal>
  )
}
