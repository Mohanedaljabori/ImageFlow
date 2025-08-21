export const uploadImage = async (file: File) => {
  const urlRes = await fetch(
    `${process.env.REACT_APP_API_URL}/api/files/generate-upload-url?fileName=${encodeURIComponent(file.name)}`
  );

  if (!urlRes.ok) {
    throw new Error('Failed to get signed URL');
  }

  const text = await urlRes.text();
  console.log("Backend response:", text);
  const { url: signedUrl } = JSON.parse(text);


  const uploadRes = await fetch(signedUrl, {
    method: 'PUT',
    headers: {
      'Content-Type': file.type || 'application/octet-stream',
    },
    body: file,
  });

  if (!uploadRes.ok) {
    throw new Error('Upload to storage failed');
  }

  return { url: signedUrl.split('?')[0] };
};
