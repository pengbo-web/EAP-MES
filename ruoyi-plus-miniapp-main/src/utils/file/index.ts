/**
 * base64字符串转File
 * @param base64String base64字符串
 * @param fileName 文件名
 * @returns
 */
export function base64StringToFile(base64String: string, fileName: string): File {
  const arr = base64String.split(',');
  const mimeMatch = arr[0].match(/:(.*?);/);
  const mime = mimeMatch && mimeMatch.length >= 2 ? mimeMatch[1] : '';
  const bstr = atob(arr[1]);

  let n = bstr.length;
  const u8arr = new Uint8Array(n);

  while (n--) {
    u8arr[n] = bstr.charCodeAt(n);
  }

  const blob = new Blob([u8arr], { type: mime });
  return new File([blob], fileName);
}
