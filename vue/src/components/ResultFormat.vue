<!-- ResultItem.vue -->
<template>
  <div class="result-item">
    <div class="result-content">
      <a class="result-link" :href="formattedLink" target="_blank">{{ formattedLinkText }}</a>
      <p class="result-title" v-html="formattedDocId"></p>
      <p class="result-weight" v-html="formattedWeight"></p>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    result: String,
    baseUrl: String,
  },
  computed: {
    formattedDocId() {
      // Extract the Document ID
      const docIdRegex = /DOC ID: (\S+)/;
      const match = this.result.match(docIdRegex);
      const extractedDocId = match ? match[1] : 'Title Not Available';
      const textBeforeDocId = 'Document ID: ';
      return extractedDocId ? `${textBeforeDocId}${extractedDocId}` : 'Title Not Available';
    },
    formattedWeight() {
      // Extract the Weight
      const weightRegex = /WEIGHT: (.+)$/m;
      const match = this.result.match(weightRegex);
      const extractedWeight = match ? match[1] : 'Weight Not Available';
      const textBeforeWeight = 'Weight: ';
      return extractedWeight ? `${textBeforeWeight}${extractedWeight}` : 'Weight Not Available';
    },
    formattedLink() {
      // Construct the link URL using the base URL and document name
      const docNameRegex = /DOC NAME: (\S+)/;
      const match = this.result.match(docNameRegex);
      const docName = match ? match[1] : '';
      return `${this.baseUrl}/${encodeURIComponent(docName)}`;
    },
    formattedLinkText() {
      const docNameRegex = /DOC NAME: (\S+)/;
      const match = this.result.match(docNameRegex);
      return match ? `Document Name: ${match[1]}` : 'Document Name: Not Available';
    },
  },
};
</script>

<style scoped>
.result-item {
  margin-bottom: 20px;
}

.result-content {
  border: 1px solid #E0E0E0;
  padding: 15px;
  border-radius: 5px;
}

.result-title {
  font-size: 18px;
  margin-bottom: 5px;
  color: #1A0DAB;
}

.result-weight {
  margin-bottom: 10px;
  color: #545454;
}

.result-link {
  color: #1A0DAB;
  text-decoration: none;
  display: block;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  font-size: 22px;
}

.result-link:hover {
  text-decoration: underline;
}
</style>
