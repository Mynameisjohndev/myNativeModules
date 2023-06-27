const convertHzToMhz = (hz) => {
  if(!hz) return null;
  const mhz = hz / 1000000;
  return mhz.toFixed(2);
}

export { convertHzToMhz }