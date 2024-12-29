import React, { useState } from 'react';
import { addExercise } from '../utils/dataUtils'; // Ajustez cette fonction selon votre gestion des données

export function ExerciceUpdate({updateItem,setView}) {
  const [formData, setFormData] = useState({
    nom: '',
    description: '',
    categorie: '',
  });
  const [imageFile, setImageFile] = useState(null);
  const [videoFile, setVideoFile] = useState(null);

  const handleImageChange = (e) => {
    setImageFile(e.target.files[0]);
  };

  const handleVideoChange = (e) => {
    setVideoFile(e.target.files[0]);
  };

  const handleSaveExercise = (exerciseData) => {
    // Vous pouvez ajouter l'exercice à un état global ou à une liste ici.
    addExercise(exerciseData);
    // Si vous avez une fonction pour récupérer les exercices, vous pouvez l'appeler ici.
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const exerciseData = new FormData();
    exerciseData.append("json", JSON.stringify({
      nom: formData.nom,
      description: formData.description,
      categorie: formData.categorie,
    }));

    if (imageFile) {
      exerciseData.append("image", imageFile);
    }

    if (videoFile) {
      exerciseData.append("vedio", videoFile);
    }

    // Vous pouvez remplacer l'URL ci-dessous par l'URL de votre backend
    const response = await fetch('http://localhost:8083/api/exercices/create', {
      method: 'POST',
      body: exerciseData,
    });

    const result = await response.text();

    if (response.ok) {
      setFormData({ nom: '', description: '', categorie: ''});
      setImageFile(null);
      setVideoFile(null);
      handleSaveExercise({
        nom: formData.nom,
        description: formData.description,
        categorie: formData.categorie,
      });
    } else {
      console.log('Erreur lors de la création de l\'exercice: ' + result);
    }
  };

  return (
    <div className="bg-white shadow sm:rounded-lg w-1/2">
      <div className="px-4 py-5 sm:p-6">
        <h3 className="text-lg font-medium leading-6 text-gray-900">New Exercise</h3>
        <div className="mt-6">
          <form onSubmit={handleSubmit} className="space-y-6">
            <div>
              <label className="block text-sm font-medium text-gray-700">Name</label>
              <input
                type="text"
                required
                value={formData.nom}
                onChange={(e) => setFormData({ ...formData, nom: e.target.value })}
                className="mt-1 block w-full rounded-md border border-gray-300 px-3 py-2 shadow-sm focus:outline-none focus:border-blue-500 focus:ring-blue-500 focus:ring-1"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700">Categorie</label>
              <input
                type="text"
                required
                value={formData.categorie}
                onChange={(e) => setFormData({ ...formData, categorie: e.target.value })}
                className="mt-1 block w-full rounded-md border border-gray-300 px-3 py-2 shadow-sm focus:outline-none focus:border-blue-500 focus:ring-blue-500 focus:ring-1"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700">Description</label>
              <textarea
                required
                value={formData.description}
                onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                className="mt-1 block w-full rounded-md border border-gray-300 px-3 py-2 shadow-sm focus:outline-none focus:border-blue-500 focus:ring-blue-500 focus:ring-1"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700">Image</label>
              <input
                type="file"
                onChange={handleImageChange}
                accept="image/*"
                className="mt-1 block w-full rounded-md border border-gray-300 px-3 py-2 shadow-sm focus:outline-none focus:border-blue-500 focus:ring-blue-500 focus:ring-1"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700">Video</label>
              <input
                type="file"
                onChange={handleVideoChange}
                accept="video/*"
                className="mt-1 block w-full rounded-md border border-gray-300 px-3 py-2 shadow-sm focus:outline-none focus:border-blue-500 focus:ring-blue-500 focus:ring-1"
              />
            </div>

            <button
              type="submit"
              className="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
            >
              Save Exercise
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}