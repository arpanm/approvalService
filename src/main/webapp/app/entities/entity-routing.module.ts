import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'approval-rule',
        data: { pageTitle: 'approvalServiceApp.approvalRule.home.title' },
        loadChildren: () => import('./approval-rule/approval-rule.module').then(m => m.ApprovalRuleModule),
      },
      {
        path: 'sub-rule',
        data: { pageTitle: 'approvalServiceApp.subRule.home.title' },
        loadChildren: () => import('./sub-rule/sub-rule.module').then(m => m.SubRuleModule),
      },
      {
        path: 'sub-rule-in-list-item',
        data: { pageTitle: 'approvalServiceApp.subRuleInListItem.home.title' },
        loadChildren: () => import('./sub-rule-in-list-item/sub-rule-in-list-item.module').then(m => m.SubRuleInListItemModule),
      },
      {
        path: 'approver',
        data: { pageTitle: 'approvalServiceApp.approver.home.title' },
        loadChildren: () => import('./approver/approver.module').then(m => m.ApproverModule),
      },
      {
        path: 'approval-request',
        data: { pageTitle: 'approvalServiceApp.approvalRequest.home.title' },
        loadChildren: () => import('./approval-request/approval-request.module').then(m => m.ApprovalRequestModule),
      },
      {
        path: 'approval-request-item',
        data: { pageTitle: 'approvalServiceApp.approvalRequestItem.home.title' },
        loadChildren: () => import('./approval-request-item/approval-request-item.module').then(m => m.ApprovalRequestItemModule),
      },
      {
        path: 'individual-approval-status',
        data: { pageTitle: 'approvalServiceApp.individualApprovalStatus.home.title' },
        loadChildren: () =>
          import('./individual-approval-status/individual-approval-status.module').then(m => m.IndividualApprovalStatusModule),
      },
      {
        path: 'approval-level-status',
        data: { pageTitle: 'approvalServiceApp.approvalLevelStatus.home.title' },
        loadChildren: () => import('./approval-level-status/approval-level-status.module').then(m => m.ApprovalLevelStatusModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
