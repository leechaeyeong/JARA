<template>
  <v-container grid-list-xs>
    <v-layout column>
        <v-card flat style='padding:0' class='comments'>
          <v-card-title class="pb-1">
            <v-icon v-if="psas[comment.writer]==null" x-large>mdi-account-circle</v-icon>
            <v-avatar v-else><img :src="psas[comment.writer]"></v-avatar>
            {{ users[comment.writer] }} · <span v-if="!comment.updated_at" class="grey--text">{{comment.created_at | filterCreated}}</span>
            <span v-else class="grey--text">{{comment.updated_at | filterCreated}} <p style="font-size: x-small; display: inline-block; margin: 0;" class="grey--text">(수정됨)</p></span>
          </v-card-title>
          <v-card-text v-if="!isChange" class='pt-0 pb-0 black--text'> {{ comment.contents }} </v-card-text>
          <v-card-text v-if="isChange" class='pt-0 pb-0'>
            <v-text-field
              ref="contents"
              v-model="change_comment.contents"
              placeholder="수정할 댓글을 입력해 주세요."
              required
              @keyup.enter="updateComment"
              color="green darken-2"
            ></v-text-field>
          </v-card-text>
          <v-card-actions class='pr-3'>
            <v-spacer></v-spacer>
            <v-row justify="end">
              <v-btn v-if="comment.writer == $store.state.userInfo.id" @click="flagComment" text small color="primary">수정</v-btn>
              <v-btn v-if="comment.writer == $store.state.userInfo.id" @click="deleteComment" text small color="error">삭제</v-btn>
            </v-row>
          </v-card-actions>
          <v-divider></v-divider>
        </v-card>
    </v-layout>
  </v-container>
</template>

<script>
import { mapState } from 'vuex'

export default {
  name: 'BarterComments',
  props: {
    comment: {
      type: Object
    }
  },
  data() {
    return {
      commentId: null,
      isChange: false,
      change_comment : {
        id: null,
        contents: '',
        writer: this.$store.state.userInfo.id,
        item_id: this.$route.params.item_id
      },
    }
  },
  methods: {
    flagComment() {
      if (this.isChange) {
        this.change_comment.contents = ''
        this.change_comment.id = null
        this.isChange = false
      } else {
        this.change_comment.contents = this.comment.contents
        this.change_comment.id = this.comment.id
        this.isChange = true
      }
    },
    updateComment() {
      if (this.$store.state.userInfo.id == this.comment.writer) {
        if (this.comment.contents != this.change_comment) {
          this.$emit('change_comment', this.change_comment)
          this.isChange = false
        } else {
          alert('수정 된 내용이 없습니다.')
        }
      } else {
        alert('댓글 작성자만 수정 가능 합니다.')
      }
    },
    deleteComment() {
      if (this.$store.state.userInfo.id == this.comment.writer) {
        this.commentId = this.comment.id
        if (this.commentId) {
          const response = confirm('정말로 삭제 하시겠습니까?')
          if (response) {
            this.$emit('find_commentId', this.commentId)
          }
        }
      } else {
        alert('댓글 작성자만 삭제 가능 합니다.')
      }
    }
  },
  computed: {
    ...mapState([
      'users',
      'userInfo',
      'psas'
    ])
  }
}
</script>

<style>

</style>