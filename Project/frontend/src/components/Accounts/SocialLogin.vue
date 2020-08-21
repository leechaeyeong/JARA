<template>
  <v-container>
  </v-container>
</template>

<script>
import { mapMutations, mapActions } from 'vuex'

export default {
  name: 'SocialLogin',
  computed: {
    ...mapMutations([
      'SET_TOKEN',
    ])
  },
  created() {
    this.getToken()
  },
  methods: {
    ...mapActions([
      'getUser'
    ]),
    getToken() {
      this.$store.commit('SET_TOKEN', this.$route.query.token)
      this.$store.dispatch('getUser')
        .then(() => this.$router.push('/accounts/social/first'))
    }
  },
}
</script>

<style scoped>
</style>