import './App.css';
import { useFileUpload } from './hooks/useFileUpload';
import FileUploadForm from './components/FileUploadForm';
import ImagePreview from './components/ImagePreview';
import { uploadImage } from './api/uploadService';

function App() {
  const { file, preview, selectFile } = useFileUpload();

  const handleUpload = async () => {
    if (!file) return;
    try {
      const data = await uploadImage(file);
      alert(`Uploaded successfully: ${data.url}`);
    } catch (err) {
      console.error(err);
      alert('Upload failed');
    }
  };

  return (
    <div className="App">
      <header className="App-header">
        <h1>Upload an Image</h1>
        <FileUploadForm onFileSelect={selectFile} onSubmit={handleUpload} />
        {preview && <ImagePreview src={preview} />}
      </header>
    </div>
  );
}

export default App;
