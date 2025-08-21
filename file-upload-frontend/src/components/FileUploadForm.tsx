import React from 'react';

interface Props {
  onFileSelect: (file: File) => void;
  onSubmit: () => void;
}

const FileUploadForm: React.FC<Props> = ({ onFileSelect, onSubmit }) => {
  return (
    <form
      onSubmit={(e) => {
        e.preventDefault();
        onSubmit();
      }}
    >
      <input
        type="file"
        accept="image/*"
        onChange={(e) => {
          const file = e.target.files?.[0];
          if (file) onFileSelect(file);
        }}
      />
      <br />
      <button type="submit">Upload Image</button>
    </form>
  );
};

export default FileUploadForm;
