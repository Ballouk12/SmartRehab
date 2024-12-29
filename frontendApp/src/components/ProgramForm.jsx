import React, { useState, useEffect } from 'react';

export function ProgramForm({ selectedPatient }) {
  const [exercises, setExercises] = useState([]);
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    patientId: selectedPatient?.id,
    exerciseIds: [],
  });

      useEffect(() => {
         const fetchExercises = async () => {
        try {
            const response = await fetch("http://localhost:8083/api/exercices/all", {
                method: "GET",
                headers: {
                    "Content-Type": "application/json"
                },
                credentials: "include" // Inclure les informations d'identification si nécessaire
            });
    
            if (!response.ok) {
                throw new Error(`Erreur: ${response.status}`);
            }
    
            const data = await response.json();
            setExercises(data); // Retourne les exercices récupérés
        } catch (error) {
            console.error("Erreur lors de la récupération des exercices :", error);
            throw error; // Relance l'erreur pour la gestion en amont
        }
    };
    fetchExercises();
      }, []);

      async function handleSaveProgram(formData) {
        try {
          const response = await fetch('http://localhost:8083/api/programs/create', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify(formData),
            credentials: 'include', // Inclure les informations d'identification si nécessaire
          });
      
          if (!response.ok) {
            throw new Error(`Erreur: ${response.status}`);
          }
      
          const savedProgram = await response.json();
          console.log('Programme enregistré :', savedProgram);
          // Vous pouvez ajouter ici d'autres actions, comme la mise à jour de l'état de l'application
        } catch (error) {
          console.error('Erreur lors de l\'enregistrement du programme :', error);
          // Vous pouvez ajouter ici la gestion des erreurs, comme l'affichage d'un message d'erreur à l'utilisateur
        }
      }
      
  const handleSubmit = (e) => {
    e.preventDefault();
    handleSaveProgram(formData); // Assurez-vous d'implémenter cette méthode pour sauvegarder les données
    setFormData({ name: '', description: '', patientId: selectedPatient.id, exerciseIds: [] });
  };

  const toggleExercise = (exerciseId) => {
    if (formData.exerciseIds.includes(exerciseId)) {
      // Si l'exercice est déjà sélectionné, le supprimer
      setFormData({
        ...formData,
        exerciseIds: formData.exerciseIds.filter((id) => id !== exerciseId),
      });
    } else {
      // Sinon, l'ajouter
      setFormData({
        ...formData,
        exerciseIds: [...formData.exerciseIds, exerciseId],
      });
    }
  };

  return (
    <div className="bg-white shadow sm:rounded-lg">
      <div className="px-4 py-5 sm:p-6">
        <h3 className="text-lg font-medium leading-6 text-gray-900">
          New Program for {selectedPatient.nom} {selectedPatient.prenom}
        </h3>
        <div className="mt-6">
          <form onSubmit={handleSubmit} className="space-y-6">
            <div>
              <label className="block text-sm font-medium text-gray-700">Program Name</label>
              <input
                type="text"
                required
                value={formData.name}
                onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700">Description</label>
              <textarea
                required
                value={formData.description}
                onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
              />
            </div>

            <div className="space-y-4">
              <label className="block text-sm font-medium text-gray-700">Select Exercises</label>
              <div className="space-y-2">
                {exercises.map((exercise) => (
                  <div key={exercise.exercice.id} className="flex items-center">
                    <input
                      type="checkbox"
                      id={`exercise-${exercise.exercice.id}`}
                      checked={formData.exerciseIds.includes(exercise.exercice.id)}
                      onChange={() => toggleExercise(exercise.exercice.id)}
                      className="h-4 w-4 text-blue-600 border-gray-300 rounded focus:ring-blue-500"
                    />
                    <label
                      htmlFor={`exercise-${exercise.exercice.id}`}
                      className="ml-2 text-sm text-gray-700"
                    >
                      {exercise.exercice.nom}
                    </label>
                  </div>
                ))}
              </div>
            </div>

            <button
              type="submit"
              className="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
            >
              Save Program
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}
