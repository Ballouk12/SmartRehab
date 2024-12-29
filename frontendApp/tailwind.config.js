/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./src/**/*.{js,jsx,ts,tsx}", // Inclut tous les fichiers dans src et sous-dossiers
    "./src/components/**/*.{js,jsx,ts,tsx}", // Spécifiquement pour src/components
    "./public/index.html", // Si vous utilisez un fichier index.html
  ],
  theme: {
    extend: {},
  },
  plugins: [require('daisyui')],
};
