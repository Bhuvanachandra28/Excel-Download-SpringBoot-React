import React from 'react';

function Sri() {
  const handleDownload = () => {
    fetch('/api/export-employees')
      .then(response => response.blob())
      .then(blob => {
        const url = window.URL.createObjectURL(new Blob([blob]));
        const a = document.createElement('a');
        a.href = url;
        a.download = 'employees.xlsx';
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
      });
  };

  return (
    <div>
      <button onClick={handleDownload}>Download Employees</button>
    </div>
  );
}

export default Sri;
