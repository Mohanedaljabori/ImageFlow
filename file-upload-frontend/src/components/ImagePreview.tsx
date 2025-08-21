import React from 'react';

interface Props {
  src: string;
}

const ImagePreview: React.FC<Props> = ({ src }) => {
  return (
    <div className="preview-container">
      <h2>Preview:</h2>
      <img src={src} alt="preview" className="preview-image" />
    </div>
  );
};

export default ImagePreview;
