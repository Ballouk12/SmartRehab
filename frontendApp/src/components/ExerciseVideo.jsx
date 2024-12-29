import React, { useState } from 'react';
import ReactPlayer from 'react-player';
import { Modal, Button } from 'antd';

const ExerciseVideo = ({ videoBase64 }) => {
  const [visible, setVisible] = useState(false);

  const showModal = () => {
    setVisible(true);
  };

  const handleCancel = () => {
    setVisible(false);
  };

  const videoUrl = `data:video/mp4;base64,${videoBase64}`;

  return (
    <>
      <a
        href="#"
        onClick={showModal}
        className="mt-4 inline-block text-blue-600 hover:text-blue-500 font-medium transition-colors"
      >
        Watch Video
      </a>
      <Modal
        visible={visible}
        onCancel={handleCancel}
        footer={null}
        width={800}
        centered
      >
        <div className="aspect-w-16 aspect-h-9">
          <ReactPlayer
            url={videoUrl}
            controls
            width="100%"
            height="100%"
          />
        </div>
      </Modal>
    </>
  );
};

export default ExerciseVideo;