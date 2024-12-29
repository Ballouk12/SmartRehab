import { mockPatients, mockExercises } from '../data/mockData';

// Utility functions for data management
// TODO: Replace with API calls when backend is ready

let patients = [...mockPatients];
let exercises = [...mockExercises];

export const getPatients = () => patients;

export const addPatient = (patient) => {
  const newPatient = {
    ...patient,
    id: Date.now().toString(),
    programs: []
  };
  patients = [...patients, newPatient];
  return newPatient;
};

export const getExercises = () => exercises;

export const addExercise = (exercise) => {
  const newExercise = {
    ...exercise,
    id: Date.now().toString()
  };
  exercises = [...exercises, newExercise];
  return newExercise;
};

export const addProgramToPatient = (patientId, program) => {
  const newProgram = {
    ...program,
    id: Date.now().toString()
  };
  
  patients = patients.map(patient => {
    if (patient.id === patientId) {
      return {
        ...patient,
        programs: [...patient.programs, newProgram]
      };
    }
    return patient;
  });
  
  return newProgram;
};