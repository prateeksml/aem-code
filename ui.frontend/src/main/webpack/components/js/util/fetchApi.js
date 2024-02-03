export const fetchApi = async (url, data) => {
  let results;
  try {
    results = await fetch(url, {
      method: "POST",
      body: JSON.stringify(data),
      headers: { "Content-type": "application/json" },
    });
    if (results.status >= 200 && results.status <= 299) {
      results = await results.json();
    } else {
      results = [];
    }
    return results;
  } catch (error) {
    console.log("Api failed-", error);
  }
};
