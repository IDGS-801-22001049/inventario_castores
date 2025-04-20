module.exports = {
  content: [
    "./src/main/resources/templates/**/*.html",
    "./src/main/resources/static/js/**/*.js",
    "./src/main/java/**/*.java"
  ],
  theme: {
    extend: {
      colors: {
        'primary': '#1e40af',
        'secondary': '#9333ea',
      },
    },
  },
  plugins: [
    require('@tailwindcss/forms'),
    require('@tailwindcss/typography'),
  ],
}