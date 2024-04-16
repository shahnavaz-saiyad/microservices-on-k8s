import React, { useState } from 'react';
import { TextField, Button, Typography, Container, Grid, Paper } from '@mui/material';
import axios from 'axios';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const Registration = () => {
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    username: '',
    organisationName: '',
    organisationDetails: '',
    encryptedDataSource: ''
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await axios.post('http://localhost:8080/tenant-service/tenants/', {
        tenantName: formData.organisationName,
        tenantDescription: formData.organisationDetails,
        encryptedDataSource: formData.encryptedDataSource,
        user: {
          firstName: formData.firstName,
          lastName: formData.lastName,
          email: formData.email,
          password: formData.password,
          username: formData.username
        }
      });
      
      toast.info('Request submitted');
      
      const eventSource = new EventSource(`http://localhost:8080/tenant-service/tenants/${response.data.tenantUuid}/sse/subscribe`);
      eventSource.onmessage = (event) => {
        console.log(event.data)
        const response = JSON.parse(event.data)
        console.log('before')
        if(response.type === 'error'){
            toast.error(response.message)
        }else{
           toast.success(response.message);
        }
        console.log('after')
      };

      console.log(response.data); // Log the response data
      // You can handle the response here, like showing a success message or redirecting the user
    } catch (error) {
      console.error('Error registering tenant:', error);
      // Handle any errors here, like showing an error message to the user
    }
  };

  return (
    <Container maxWidth="md" style={{ marginTop: '50px' }}>
      <Paper elevation={3} style={{ padding: '30px' }}>
        <Typography variant="h4" gutterBottom style={{ textAlign: 'center', marginBottom: '20px' }}>
          Registration
        </Typography>
        <form onSubmit={handleSubmit}>
          <Grid container spacing={2}>
            <Grid item xs={6}>
              <TextField
                fullWidth
                label="First Name"
                name="firstName"
                value={formData.firstName}
                onChange={handleChange}
                required
              />
            </Grid>
            <Grid item xs={6}>
              <TextField
                fullWidth
                label="Last Name"
                name="lastName"
                value={formData.lastName}
                onChange={handleChange}
                required
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                fullWidth
                label="Email"
                name="email"
                type="email"
                value={formData.email}
                onChange={handleChange}
                required
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                fullWidth
                label="Password"
                name="password"
                type="password"
                value={formData.password}
                onChange={handleChange}
                required
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                fullWidth
                label="Username"
                name="username"
                value={formData.username}
                onChange={handleChange}
                required
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                fullWidth
                label="Organization Name"
                name="organisationName"
                value={formData.organisationName}
                onChange={handleChange}
                required
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                fullWidth
                label="Organization Details"
                name="organisationDetails"
                multiline
                rows={4}
                value={formData.organisationDetails}
                onChange={handleChange}
                required
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                fullWidth
                label="Encrypted Data Source"
                name="encryptedDataSource"
                multiline
                rows={4}
                value={formData.encryptedDataSource}
                onChange={handleChange}
                required
              />
            </Grid>
          </Grid>
          <Button type="submit" variant="contained" color="primary" fullWidth style={{ marginTop: '20px' }}>
            Register
          </Button>
        </form>
      </Paper>
      <ToastContainer />
    </Container>
  );
};

export default Registration;
