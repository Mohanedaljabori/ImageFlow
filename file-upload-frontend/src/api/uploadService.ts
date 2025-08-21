export const uploadImage = async (file: File) => {
  const formData = new FormData();
  formData.append('image', file);
  const res = await fetch(`${process.env.REACT_APP_API_URL}/api/files/upload`, {
    method: 'POST',
    body: formData
  });
  if (!res.ok) throw new Error('Upload failed');
  return res.json();
};
