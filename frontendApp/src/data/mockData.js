// Mock data for demonstration purposes
// TODO: Replace with API calls when backend is ready

export const mockPatients = [
  {
    id: '1',
    firstName: 'John',
    lastName: 'Doe',
    email: 'john.doe@example.com',
    phone: '(555) 123-4567',
    dateOfBirth: '1990-05-15',
    programs: []
  },
  {
    id: '2',
    firstName: 'Jane',
    lastName: 'Smith',
    email: 'jane.smith@example.com',
    phone: '(555) 987-6543',
    dateOfBirth: '1985-08-22',
    programs: []
  }
];

export const mockExercises = [
  {
    id: '1',
    name: 'Push-ups',
    description: 'Basic upper body exercise',
    videoUrl: 'https://example.com/pushups.mp4',
    imageUrl: 'https://images.unsplash.com/photo-1566241142559-40e1dab266c6',
    inputs: [
      { name: 'Sets', type: 'number' },
      { name: 'Reps', type: 'number' },
      { name: 'Rest Time', type: 'number' }
    ]
  },
  {
    id: '2',
    name: 'Squats',
    description: 'Lower body strengthening exercise',
    videoUrl: 'https://example.com/squats.mp4',
    imageUrl: 'https://images.unsplash.com/photo-1566241142559-40e1dab266c6',
    inputs: [
      { name: 'Sets', type: 'number' },
      { name: 'Reps', type: 'number' },
      { name: 'Weight (kg)', type: 'number' }
    ]
  }
];