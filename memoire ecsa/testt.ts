
// src/api/axios.ts
const api = axios.create({ baseURL: import.meta.env.VITE_API_BASE_URL });

api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const original = error.config;
    if (error.response?.status === 401 && !original._retry) {
      original._retry = true;
      const refreshToken = localStorage.getItem(REFRESH_KEY);
      const { data } = await api.post('/v1/auth/refresh', { refreshToken });
      localStorage.setItem(TOKEN_KEY, data.data.accessToken);
      original.headers.Authorization = `Bearer ${data.data.accessToken}`;
      return api(original);   // Rejoue la requete originale
    }
    if (error.response?.status === 401)
      useAuthStore.getState().logout(); // Deconnexion si refresh echoue
    return Promise.reject(error);
  }
);